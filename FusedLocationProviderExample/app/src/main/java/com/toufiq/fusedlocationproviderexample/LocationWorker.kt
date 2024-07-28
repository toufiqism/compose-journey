import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationRequest.*
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlin.math.ln


class LocationWorker(context: Context, workerParams: WorkerParameters) :
    CoroutineWorker(context, workerParams) {
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    private var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    private var mLocationRequest: LocationRequest? = null
    private var mLocationCallback: LocationCallback? = null
    private val LOCATION_REQUEST_INTERVAL: Long = 5000
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override suspend fun doWork(): Result {
        return try {
            mFusedLocationProviderClient =
                LocationServices.getFusedLocationProviderClient(applicationContext)
            createLocationRequest()
            mLocationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    val lat = locationResult.lastLocation!!.latitude
                    val lng = locationResult.lastLocation!!.longitude
                    Log.e("LocationWorker", "$lat and  ${lng}")

                }
            }
            requestLocationUpdate()
            Result.success()
        } catch (e: Exception) {
            Log.e("LocationWorker", "Error getting location", e)
            Result.failure()
        }
    }

    private fun createLocationRequest() {
        mLocationRequest = LocationRequest.create()
        mLocationRequest!!.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        mLocationRequest!!.setInterval(LOCATION_REQUEST_INTERVAL).fastestInterval =
            LOCATION_REQUEST_INTERVAL

    }

    private fun requestLocationUpdate() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            Log.e("permission", "denied");
            return
        }

        mFusedLocationProviderClient!!.requestLocationUpdates(
            mLocationRequest!!,
            mLocationCallback!!,
            Looper.myLooper()
        )
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        } else {

            fusedLocationClient.lastLocation.addOnSuccessListener {

                Log.d("LocationWorker", it.latitude.toString())

            }
        }
    }

    fun checkLocation() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, 15000)
            getLastLocation()

        }.also { runnable = it }, 15000)
    }
}