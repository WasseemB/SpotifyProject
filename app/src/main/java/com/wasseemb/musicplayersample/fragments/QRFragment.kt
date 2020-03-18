package com.wasseemb.musicplayersample.fragments


import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.print.PrintHelper
import com.google.android.material.button.MaterialButton
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
import com.wasseemb.musicplayersample.R
import com.wasseemb.musicplayersample.R.layout
import com.wasseemb.musicplayersample.extensions.PreferenceHelper.defaultPrefs
import com.wasseemb.musicplayersample.extensions.PreferenceHelper.get
import com.wasseemb.musicplayersample.extensions.PreferenceHelper.set
import com.wasseemb.musicplayersample.utils.FIREBASE_UNIQUE_ID
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [QRFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [QRFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class QRFragment : Fragment() {
  // TODO: Rename and change types of parameters
  //private var param1: String? = null
  //private var param2: String? = null
  private lateinit var prefs: SharedPreferences
  private var firebaseName: String? = null

  //private var listener: OnFragmentInteractionListener? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    arguments?.let {
      //param1 = it.getString(ARG_PARAM1)
      //param2 = it.getString(ARG_PARAM2)
    }
    context?.let {
      prefs = defaultPrefs(it)
    }
    firebaseName = prefs[FIREBASE_UNIQUE_ID, ""]
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
      savedInstanceState: Bundle?): View? {
    // Inflate the layout for this fragment
    val view = inflater.inflate(layout.fragment_qr, container, false)

    val fab = activity?.findViewById<MaterialButton>(R.id.fab)
    fab?.visibility = View.INVISIBLE
    val qrText = view.findViewById<EditText>(R.id.txtQr)
    val qrImage = view.findViewById<ImageView>(R.id.imgQr)

    if (!firebaseName.equals("")) {
      qrText.setText(firebaseName)
      disableEditText(qrText)
      qrImage.setImageBitmap(createQrCode(firebaseName.toString()))
    }

    bindTo(view)
    return view
  }


  fun createQrCode(text: String): Bitmap? {
    val multiFormatWriter = MultiFormatWriter()
    var bitmap: Bitmap? = null
    try {
      val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250)
      val barcodeEncoder = BarcodeEncoder()
      prefs[FIREBASE_UNIQUE_ID] = text
      bitmap = barcodeEncoder.createBitmap(bitMatrix)

      // doPhotoPrint(bitmap)

    } catch (e: WriterException) {
      e.printStackTrace()
    }
    return bitmap
  }

  private fun disableEditText(editText: EditText) {
    editText.isFocusable = false
    editText.isEnabled = false
    editText.isCursorVisible = false
    editText.keyListener = null
    editText.setBackgroundColor(Color.TRANSPARENT)
  }


  fun bindTo(view: View) {
    val qrText = view.findViewById<EditText>(R.id.txtQr)
    val qrImage = view.findViewById<ImageView>(R.id.imgQr)
    val qrMaterialButton = view.findViewById<MaterialButton>(
        R.id.mtrlBtnQr)
    val qrShare = view.findViewById<MaterialButton>(
        R.id.mtrlBtnShare)

    qrMaterialButton.setOnClickListener {
      val text = qrText.text.toString()
      var bitmap: Bitmap?
      bitmap = createQrCode(text)
      qrImage.setImageBitmap(bitmap)
    }

    qrShare.setOnClickListener {
      val text = qrText.text.toString()
      val bitmap: Bitmap? = createQrCode(text)
      qrImage.setImageBitmap(bitmap)
      bitmap?.let {
        val uri = saveImage(bitmap)
        val shareIntent: Intent = Intent().apply {
          action = Intent.ACTION_SEND
          putExtra(Intent.EXTRA_STREAM, uri)
          type = "image/png"
        }
        startActivity(Intent.createChooser(shareIntent, "Share QRCode"))
      }
    }
  }

  private fun doPhotoPrint(bitmap: Bitmap) {
    activity?.also { context ->
      PrintHelper(context).apply {
        //scaleMode = PrintHelper.SCALE_MODE_FIT
      }.also { printHelper ->
        val resizedBitmap = Bitmap.createScaledBitmap(
            bitmap, 50, 50, false)
        printHelper.printBitmap("droids.jpg - test print", resizedBitmap)
      }
    }
  }


  private fun saveImage(image: Bitmap): Uri? {
    //TODO - Should be processed in another thread
    val imagesFolder = File(context?.cacheDir, "images")
    var uri: Uri? = null
    try {
      imagesFolder.mkdirs()
      val file = File(imagesFolder, "shared_image.png")

      val stream = FileOutputStream(file)
      image.compress(Bitmap.CompressFormat.PNG, 90, stream)
      stream.flush()
      stream.close()
      uri = FileProvider.getUriForFile(context!!, "com.wasseemb.fileprovider", file)

    } catch (e: IOException) {
      Log.d(TAG, "IOException while trying to write file for sharing: " + e.message)
    }

    return uri
  }

  companion object {
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment QRFragment.
     */
    // TODO: Rename and change types and number of parameters
    @JvmStatic
    fun newInstance() =
        QRFragment().apply {
          arguments = Bundle().apply {
            //putString(ARG_PARAM1, param1)
            // putString(ARG_PARAM2, param2)
          }
        }
  }
}
