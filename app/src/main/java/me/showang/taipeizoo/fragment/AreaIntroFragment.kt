package me.showang.taipeizoo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import github.showang.kat.assignN
import me.showang.recyct.RecyctAdapter
import me.showang.recyct.RecyctViewHolder
import me.showang.recyct.items.RecyctItemBase
import me.showang.taipeizoo.R
import me.showang.taipeizoo.databinding.FragmentAreaListBinding
import me.showang.taipeizoo.model.AreaInfo
import me.showang.taipeizoo.viewmodel.AreaIntroViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class AreaIntroFragment : Fragment() {

    private val outsideList = mutableListOf<AreaInfo>()
    private val insideList = mutableListOf<AreaInfo>()
    private val viewModel: AreaIntroViewModel by viewModel()

    private var binding: FragmentAreaListBinding? = null
    private var adapter: RecyctAdapter? = null
    private val categoryList: List<String> by lazy {
        listOf(getString(R.string.area_category_outside), getString(R.string.area_category_inside))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAreaListBinding.inflate(inflater)
            .assignN(::binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            initAreaRecyclerView()
        }
        viewModel.areaInfoListLiveData.observe({ lifecycle }) {
            outsideList.clear()
            insideList.clear()
            it.forEach { area ->
                when(area.category) {
                    categoryList[0] -> outsideList.add(area)
                    categoryList[1] -> insideList.add(area)
                }
            }
            adapter?.notifyDataSetChanged()
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun FragmentAreaListBinding.initAreaRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = RecyctAdapter(outsideList, insideList).apply {
            registerHeader(AreaIntroTitleItem())
            sectionsByGroup(SectionTitleItem(), categoryList)
            register(AreaInfoItem()) { data, _ ->
                when (data) {
                    is AreaInfo -> findNavController().navigate(
                        R.id.action_area_detail, Bundle().apply {
                            putSerializable(AreaDetailFragment.INPUT_AREA_INFO, data)
                        })
                }
            }
        }.assignN(::adapter)
    }

    class AreaInfoItem : RecyctItemBase() {
        override fun create(inflater: LayoutInflater, parent: ViewGroup) = object :
            RecyctViewHolder(inflater, parent, R.layout.item_area_info) {
            private val imageView: ImageView by id(R.id.imageView)
            private val nameText: TextView by id(R.id.nameText)

            override fun bind(data: Any, atIndex: Int) {
                when (data) {
                    is AreaInfo -> bind(data)
                }
            }

            private fun bind(areaInfo: AreaInfo) {
                Glide.with(imageView).load(areaInfo.imageUrl).into(imageView)
                nameText.text = areaInfo.name.replace("(", "\n(")
            }
        }
    }

    class AreaIntroTitleItem : RecyctItemBase() {
        override fun create(inflater: LayoutInflater, parent: ViewGroup) =
            object : RecyctViewHolder(inflater, parent, R.layout.item_header_area_intro_title) {
                override fun bind(data: Any, atIndex: Int) {}
            }
    }

    class SectionTitleItem : RecyctItemBase() {
        override fun create(inflater: LayoutInflater, parent: ViewGroup) =
            object : RecyctViewHolder(inflater, parent, R.layout.item_section_area_category) {
                private val titleText: TextView by id(R.id.titleText)
                override fun bind(data: Any, atIndex: Int) {
                    (data as? String)?.let {
                        titleText.text = it
                    }
                }
            }
    }
}