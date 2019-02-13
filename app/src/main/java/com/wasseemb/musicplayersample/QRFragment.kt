package com.wasseemb.musicplayersample

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.support.design.button.MaterialButton
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.support.v4.print.PrintHelper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.journeyapps.barcodescanner.BarcodeEncoder
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
  private var param1: String? = null
  private var param2: String? = null
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
    val view = inflater.inflate(R.layout.fragment_qr, container, false)

    val fab = activity?.findViewById<MaterialButton>(R.id.fab)
    fab?.visibility = View.INVISIBLE
    bindTo(view)
    return view
  }


  fun bindTo(view: View) {
    val qrText = view.findViewById<EditText>(R.id.txtQr)
    val qrImage = view.findViewById<ImageView>(R.id.imgQr)
    val qrMaterialButton = view.findViewById<MaterialButton>(R.id.mtrlBtnQr)
    val qrShare = view.findViewById<MaterialButton>(R.id.mtrlBtnShare)
    lateinit var bitmap: Bitmap

    qrMaterialButton.setOnClickListener {
      val text = qrText.text.toString()
      val multiFormatWriter = MultiFormatWriter()
      try {
        val bitMatrix = multiFormatWriter.encode(text, BarcodeFormat.QR_CODE, 250, 250)
        val barcodeEncoder = BarcodeEncoder()
        bitmap = barcodeEncoder.createBitmap(bitMatrix)
        qrImage.setImageBitmap(bitmap)
        // doPhotoPrint(bitmap)

      } catch (e: WriterException) {
        e.printStackTrace()
      }
    }

    qrShare.setOnClickListener {
      val uri = saveImage(bitmap)
      val shareIntent: Intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
      }
      startActivity(Intent.createChooser(shareIntent, "Share QRCode"))
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
  // TODO: Rename method, update argument and hook method into UI event
//  fun onButtonPressed(uri: Uri) {
//    listener?.onFragmentInteraction(uri)
//  }

//  override fun onAttach(context: Context) {
//    super.onAttach(context)
//    if (context is OnFragmentInteractionListener) {
//      listener = context
//    } else {
//      throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
//    }
//  }

//  override fun onDetach() {
//    super.onDetach()
//    listener = null
//  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   *
   *
   * See the Android Training lesson [Communicating with Other Fragments]
   * (http://developer.android.com/training/basics/fragments/communicating.html)
   * for more information.
   */
//  interface OnFragmentInteractionListener {
//    // TODO: Update argument type and name
//    fun onFragmentInteraction(uri: Uri)
//  }

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
    fun newInstance(param1: String, param2: String) =
        QRFragment().apply {
          arguments = Bundle().apply {
            putString(ARG_PARAM1, param1)
            putString(ARG_PARAM2, param2)
          }
        }
  }
}
