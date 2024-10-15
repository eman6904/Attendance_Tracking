import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.graphics.Rect
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavHostController
import com.example.qrcodescanner.data.model.AttendanceRegistrationRequirements
import com.example.qrcodescanner.data.utils.getCurrentCamp
import com.example.qrcodescanner.ui.components.rejectedTrainee
import com.example.qrcodescanner.R
import com.example.qrcodescanner.data.apis.ViewModel2
import com.example.qrcodescanner.ui.components.scannerErrorDialog
import com.example.qrcodescanner.navigation.ScreensRoute
import com.google.common.util.concurrent.ListenableFuture
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@Composable
fun QrScannerScreen(navController: NavHostController) {

    val barcodeValue = remember { mutableStateOf("") }
    val errorMessage = remember { mutableStateOf("") }
    val message = remember { mutableStateOf("") }
    val shutDownError = remember { mutableStateOf(false) }
    val shutDownFailedDialog = remember { mutableStateOf(false) }
    val showProgress = remember { mutableStateOf(false) }
    val viewModel2= ViewModel2()
    scannerErrorDialog(shutDownError, errorMessage,showProgress,barcodeValue)

    if (shutDownFailedDialog.value) {
        rejectedTrainee(
            shutDown = shutDownFailedDialog,
            message = message,
            barcodeValue = barcodeValue,
            showProgress =showProgress
        )
    }
    if (barcodeValue.value.isNotEmpty()) {

        LaunchedEffect(Unit) {
            showProgress.value=true
            viewModel2.addTraineeToAttendance(
                traineeRequirements = AttendanceRegistrationRequirements(
                    barcodeValue.value,
                    getCurrentCamp()!!.id
                ),
                navController = navController,
                errorMessage = errorMessage,
                shutDownError = shutDownError,
                message = message,
                shutDownFailedDialog = shutDownFailedDialog,
                showProgress = showProgress
            )
        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .border(10.dp, color = colorResource(id = R.color.mainColor))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        QRCodeComposable(barcodeValue,showProgress)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.mainColor))
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.icpc_logo_night),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
            )
            Text(
                text = stringResource(R.string.icpc_attendance),
                style = MaterialTheme.typography.h6.copy(color = Color.White)
            )
            Image(
                painter = painterResource(R.drawable.icpc_logo_night),
                contentDescription = null,
                modifier = Modifier
                    .size(35.dp)
                    .scale(scaleX = -1f, scaleY = 1f) // Flip the image horizontally
            )
        }
    }
    BackHandler() {
        navController.navigate(ScreensRoute.MainScreen.route)
    }
}

@RequiresApi(Build.VERSION_CODES.M)
@Composable
fun QRCodeComposable(
    barcodeValuee: MutableState<String>,
    showProgress:MutableState<Boolean>
) {
    val context = LocalContext.current
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(context)

    DisposableEffect(cameraProviderFuture) {
        onDispose {
            cameraProviderFuture.get().unbindAll()
        }
    }
    var hasCamPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            hasCamPermission = granted
        }
    )
    LaunchedEffect(key1 = true) {
        launcher.launch(Manifest.permission.CAMERA)
    }
    if (hasCamPermission) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(id = R.color.mainColor)),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    text = stringResource(R.string.qrcode_scanner),
                    style = MaterialTheme.typography.h6.copy(color = Color.White),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    textAlign = TextAlign.Center
                )
            }
            Box(
                modifier = Modifier
                    .width(310.dp)
                    .height(390.dp)
                    .padding(30.dp)
                //  .border(6.dp, color = colorResource(id = R.color.yelllow))
                , contentAlignment = Alignment.Center
            ) {
                if(showProgress.value){
                    CircularProgressIndicator(
                        color = Color.White, strokeWidth = 3.dp,
                        modifier = Modifier.size(50.dp)
                    )
                }else{
                    scanner(barcodeValuee = barcodeValuee)
                }
            }

        }
    }
}

@Composable
fun scanner(
    barcodeValuee: MutableState<String>
) {

    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    var preview by remember { mutableStateOf<Preview?>(null) }
    var barcodeRect by remember { mutableStateOf<Rect?>(null) }
    AndroidView(
        factory = { AndroidViewContext ->
            PreviewView(AndroidViewContext).apply {
                this.scaleType = PreviewView.ScaleType.FILL_CENTER
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            }
        },
        update = { previewView ->
            val cameraSelector: CameraSelector = CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
            val cameraExecutor: ExecutorService = Executors.newSingleThreadExecutor()
            val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
                ProcessCameraProvider.getInstance(context)

            cameraProviderFuture.addListener({
                preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()
//                val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
//                val mediaPlayer = MediaPlayer.create(context, R.raw.scan_soundd)
                val barcodeAnalyser = BarcodeAnalyser { barcodes, rect ->
                    barcodes.forEach { barcode ->
                        barcode.rawValue?.let { barcodeValue ->

//                            vibrator.vibrate(100) //for sound
//                            mediaPlayer.start()
                            barcodeValuee.value = barcodeValue
                        }
                    }
                    barcodeRect = rect
                }
                val imageAnalysis: ImageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor, barcodeAnalyser)
                    }

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                    //Log.e("qr code", e.message ?: "")
                }
            }, ContextCompat.getMainExecutor(context))
        }
    )
    // drawRectangle(barcodeRect = barcodeRect)
}

@Composable
fun drawRectangle(
    barcodeRect: Rect?
) {

    val color = colorResource(id = R.color.mainColor)
    barcodeRect?.let { rect ->
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawRect(
                color = color,
                topLeft = Offset(rect.left.toFloat(), rect.top.toFloat()),
                size = Size(
                    rect.width().toFloat(),
                    rect.height().toFloat()
                ),
                style = Stroke(width = 4.dp.toPx())
            )
        }
    }
}