package me.showang.taipeizoo.fragment

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import github.showang.kat.assignN
import me.showang.recyct.RecyctAdapter
import me.showang.recyct.RecyctViewHolder
import me.showang.recyct.items.RecyctItemBase
import me.showang.taipeizoo.R
import me.showang.taipeizoo.databinding.FragmentPlantDetailBinding
import me.showang.taipeizoo.model.ImageInfo
import me.showang.taipeizoo.model.PlantInfo

class PlantDetailFragment : Fragment() {
    companion object {
        const val INPUT_PLANT_INFO = "IPI"
    }

    private var binding: FragmentPlantDetailBinding? = null
    private val initPlantInfo: PlantInfo by lazy {
        requireArguments().getSerializable(INPUT_PLANT_INFO) as? PlantInfo
            ?: throw IllegalStateException("Should to input the PlantInfo for init")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentPlantDetailBinding.inflate(layoutInflater).assignN(::binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            initToolbar()
            initCoverPager()
            initPlantInfoLayouts()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun FragmentPlantDetailBinding.initToolbar() {
        toolbar.navigationIcon = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_arrow_back_24
        )?.apply { DrawableCompat.setTint(this, Color.BLACK) }
        toolbar.setNavigationOnClickListener { activity?.onBackPressed() }
    }

    private fun FragmentPlantDetailBinding.initCoverPager() {
        val size = initPlantInfo.imageInfoList.size
        pageCountText.isVisible = size > 1
        pageCountText.text = getString(R.string.plantDetail_pageCountText, 1, size)
        coverPager.adapter = RecyctAdapter(initPlantInfo.imageInfoList).apply {
            register(PlantImageItem())
        }
        coverPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                pageCountText.text = getString(R.string.plantDetail_pageCountText, position + 1, size)
            }
        })
    }

    private fun FragmentPlantDetailBinding.initPlantInfoLayouts() {
        nameText.text = initPlantInfo.name
        featureContentText.text = initPlantInfo.feature
        briefText.text = initPlantInfo.briefDesc
        otherNameText.text = initPlantInfo.otherCountryNames["en"] ?: ""
    }

    class PlantImageItem : RecyctItemBase() {
        override fun create(inflater: LayoutInflater, parent: ViewGroup) =
            object : RecyctViewHolder(inflater, parent, R.layout.item_plant_image) {
                private val coverImage: ImageView by id(R.id.coverImage)
                private val imageDesc: TextView by id(R.id.coverDescriptionText)

                override fun bind(data: Any, atIndex: Int) {
                    when (data) {
                        is ImageInfo -> bind(data)
                    }
                }

                private fun bind(imageInfo: ImageInfo) {
                    Glide.with(coverImage).load(imageInfo.url).centerCrop().into(coverImage)
                    imageDesc.text = imageInfo.description
                }

            }

    }
}