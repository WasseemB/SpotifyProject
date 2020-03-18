package com.wasseemb.musicplayersample.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton
import com.google.zxing.ResultPoint
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.BarcodeView
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.utils.SpotifyHelper


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QReader.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QReader.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class QRReaderFragment : Fragment() {
  // TODO: Rename and change types of parameters
  private var param1: String? = null
  private var param2: String? = null
  var dbvScanner: BarcodeView? = null

  //private var listener: OnFragmentInteractionListener? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      param1 = it.getString(ARG_PARAM1)
      param2 = it.getString(ARG_PARAM2)
    }
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(layout.fragment_qreader, container, false)
    dbvScanner = view.findViewById<BarcodeView>(
        R.id.dbv_barcode)
    val mtrlBtn = view.findViewById<MaterialButton>(R.id.connectSpotifyQR)
    requestPermission()
    dbvScanner?.cameraInstance?.cameraSettings?.requestedCameraId = 1

    dbvScanner?.decodeContinuous(object : BarcodeCallback {
      override fun barcodeResult(result: BarcodeResult) {
        mtrlBtn.text = result.text
        //updateText(result.text)
        //beepSound()
      }

      override fun possibleResultPoints(resultPoints: List<ResultPoint>) {

      }
    })
    mtrlBtn.setOnClickListener {
      SpotifyHelper().createAuthRequest(activity!!)
    }
    return view
  }

  fun requestPermission() {
    if (ContextCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA)
        != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this.activity!!,
          arrayOf(Manifest.permission.CAMERA), 0); }
  }


  override fun onRequestPermissionsResult(
      requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    if (requestCode == 0 && grantResults.size < 1) {
      requestPermission()
    } else {
      dbvScanner?.resume()
    }
  }


  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QReader.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance(param1: String, param2: String) =
        QRReaderFragment().apply {
          arguments = Bundle().apply {
            putString(ARG_PARAM1, param1)
            putString(ARG_PARAM2, param2)
          }
        }
  }
}
