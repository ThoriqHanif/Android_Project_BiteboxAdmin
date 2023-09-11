package com.thrq.biteboxadmin.fragment

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.VISIBLE
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.thrq.biteboxadmin.R
import com.thrq.biteboxadmin.adapter.AddFoodImageAdapter
import com.thrq.biteboxadmin.databinding.FragmentAddFoodBinding
import com.thrq.biteboxadmin.databinding.FragmentFoodBinding
import com.thrq.biteboxadmin.model.AddFoodModel
import com.thrq.biteboxadmin.model.CategoryModel
import java.util.UUID

class AddFoodFragment : Fragment() {

    private lateinit var binding: FragmentAddFoodBinding
    private lateinit var list : ArrayList<Uri>
    private lateinit var listImages : ArrayList<String>
    private lateinit var adapter : AddFoodImageAdapter
    private var coverImage: Uri ? = null
    private lateinit var dialog : Dialog
    private var coverImgUrl : String? = ""
    private lateinit var categoryList: ArrayList<String>
//    private lateinit var RestoList: ArrayList<String>

    private var launchGalleryActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            coverImage = it.data!!.data
            binding.foodCoverImg.setImageURI(coverImage)
            binding.foodCoverImg.visibility = VISIBLE
        }
    }

    private var launchFoodActivity = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        if (it.resultCode == Activity.RESULT_OK){
            val imageUrl = it.data!!.data
            list.add(imageUrl!!)
            adapter.notifyDataSetChanged()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddFoodBinding.inflate(layoutInflater)

        list = ArrayList()
        listImages = ArrayList()
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.progress_layout)
        dialog.setCancelable(false)

        binding.selectCoverImg.setOnClickListener{
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchGalleryActivity.launch(intent)
        }

        binding.btnFoodImg.setOnClickListener{
            val intent = Intent("android.intent.action.GET_CONTENT")
            intent.type = "image/*"
            launchFoodActivity.launch(intent)
        }

        setFoodCategory()

        adapter = AddFoodImageAdapter(list)
        binding.foodImgReycle.adapter = adapter

        binding.btnSimpanFood.setOnClickListener {
            validateData()
        }

        return binding.root
    }

    private fun validateData() {
        if (binding.foodName.text.toString().isEmpty()){
            binding.foodName.requestFocus()
            binding.foodName.error = "Lengkapi data"
        }else if(binding.foodPrice.text.toString().isEmpty()){
            binding.foodPrice.requestFocus()
            binding.foodPrice.error = "Lengkapi data"
        }else if(coverImage == null){
            Toast.makeText(requireContext(), "Silahkan pilih gambar cover", Toast.LENGTH_SHORT).show()
        }else if(list.size <1){
            Toast.makeText(requireContext(), "Silahkan pilih gambar makanan", Toast.LENGTH_SHORT).show()
        }else{
            uploadImage()
        }
    }

    private fun uploadImage() {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("foods/$fileName")
        refStorage.putFile(coverImage!!)
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image ->
                    coverImgUrl = image.toString()

                    uploadFoodImage()
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Terjadi Kesalahan storage", Toast.LENGTH_SHORT).show()
            }
    }

    private var i = 0
    private fun uploadFoodImage() {
        dialog.show()

        val fileName = UUID.randomUUID().toString()+".jpg"

        val refStorage = FirebaseStorage.getInstance().reference.child("foods/$fileName")
        refStorage.putFile(list[i])
            .addOnSuccessListener {
                it.storage.downloadUrl.addOnSuccessListener {image ->
                    listImages.add(image!!.toString())
                    if (list.size == listImages.size){
                        storeData()
                    }else{
                        i += 1
                        uploadFoodImage()
                    }
                }
            }
            .addOnFailureListener {
                dialog.dismiss()
                Toast.makeText(requireContext(), "Terjadi Kesalahan storage", Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeData() {
        val db = Firebase.firestore.collection("foods")
        val key = db.document().id

        val data = AddFoodModel(
            binding.foodName.text.toString(),
            binding.foodDesc.text.toString(),
            binding.foodPrice.text.toString(),
            categoryList[binding.foodCategory.selectedItemPosition],
//            restoList[binding.foodResto.selectedItemPosition],
            coverImgUrl.toString(),
            key,
            listImages
        )
        db.document(key).set(data).addOnSuccessListener {
            dialog.dismiss()
            Toast.makeText(requireContext(),"Berhasil menambah Makanan", Toast.LENGTH_SHORT).show()
            binding.foodName.text= null
        }

            .addOnFailureListener{
                dialog.dismiss()
                Toast.makeText(requireContext(),"Terjadi Kesalahan", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setFoodCategory(){
        categoryList = ArrayList()
        Firebase.firestore.collection("categories").get().addOnSuccessListener {
            categoryList.clear()
            for (doc in it.documents){
                val data = doc.toObject(CategoryModel::class.java)
                categoryList.add(data!!.cate!!)

            }
            categoryList.add(0, "Pilih Kategori")

            val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_kategori_item_layout, categoryList )
            binding.foodCategory.adapter = arrayAdapter
        }
    }

}