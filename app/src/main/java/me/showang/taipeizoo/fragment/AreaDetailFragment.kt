package me.showang.taipeizoo.fragment

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import github.showang.kat.assignN
import github.showang.mypainter.transtate.core.Transform
import github.showang.mypainter.transtate.core.ViewState
import me.showang.recyct.RecyctAdapter
import me.showang.recyct.RecyctViewHolder
import me.showang.recyct.items.RecyctItemBase
import me.showang.taipeizoo.R
import me.showang.taipeizoo.databinding.FragmentAreaDetailBinding
import me.showang.taipeizoo.model.AreaInfo
import me.showang.taipeizoo.model.PlantInfo
import me.showang.taipeizoo.viewmodel.AreaDetailViewModel
import me.showang.taipeizoo.viewmodel.AreaDetailViewModel.Event
import me.showang.taipeizoo.viewmodel.AreaDetailViewModel.State
import org.koin.androidx.viewmodel.ext.android.viewModel

class AreaDetailFragment : Fragment() {

    companion object {
        const val INPUT_AREA_INFO = "IAI"
    }

    private val initAreaInfo: AreaInfo by lazy {
        requireArguments().getSerializable(INPUT_AREA_INFO) as? AreaInfo
            ?: throw IllegalStateException("Should to input the AreaInfo for init")
    }
    private val plantInfoList: MutableList<PlantInfo> = mutableListOf()
    private val viewModel: AreaDetailViewModel by viewModel()
    private var binding: FragmentAreaDetailBinding? = null
    private var adapter: RecyctAdapter? = null
    private var lastOffset: Int = 0
    private var emptyPlant: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentAreaDetailBinding.inflate(layoutInflater)
            .assignN(::binding).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding?.run {
            initToolbar()
            initRecyclerView()
        }
        viewModel.observeTransformation({ lifecycle }, ::initViewByState, ::updateViewByTransform)
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun initViewByState(state: ViewState) {
        when (state) {
            is State.Initializing -> {
                adapter?.enableLoadMore = true
                adapter?.isLoadMoreFail = false
            }
            is State.LoadPlantInfoError -> {
                adapter?.isLoadMoreFail = true
            }
            is State.PlantInfoLoaded -> {
                adapter?.enableLoadMore = false
                plantInfoList.clear()
                plantInfoList.addAll(state.plantInfoList)
                adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun updateViewByTransform(transform: Transform) {
        when (transform.byEvent) {
            is Event.PlantsLoaded -> {
                adapter?.enableLoadMore = false
                plantInfoList.addAll(transform.byEvent.plantInfoList)
                if (plantInfoList.isEmpty()) {
                    emptyPlant = true
                }
                adapter?.notifyDataSetChanged()
            }
            is Event.LoadPlantsFail -> {
                adapter?.isLoadMoreFail = true
            }
            is Event.ReloadPlants -> {
                adapter?.enableLoadMore = true
                adapter?.isLoadMoreFail = false
            }
        }
    }

    private fun FragmentAreaDetailBinding.initToolbar() {
        toolbarLayout.title = initAreaInfo.name
        appbar.setExpanded(lastOffset == 0)
        toolbar.title = initAreaInfo.name
        toolbar.navigationIcon = ContextCompat.getDrawable(
            requireContext(),
            R.drawable.ic_baseline_arrow_back_24
        )?.apply {
            DrawableCompat.setTint(this, Color.WHITE)
        }
        toolbar.setNavigationOnClickListener {
            activity?.onBackPressed()
        }
        Glide.with(coverImage).load(initAreaInfo.imageUrl).into(coverImage)
    }

    private fun FragmentAreaDetailBinding.initRecyclerView() {
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = RecyctAdapter(plantInfoList).apply {
            register(PlantInfoItem()) { data, _ ->
                (data as? PlantInfo)?.let {
                    findNavController().navigate(R.id.action_plant_detail, Bundle().apply {
                        putSerializable(PlantDetailFragment.INPUT_PLANT_INFO, it)
                    })
                }
            }
            registerHeader(AreaHeaderItem { emptyPlant }, initAreaInfo)
            defaultLoadMore { viewModel.loadPlantInfo(initAreaInfo.name) }
        }.assignN(::adapter)
        recycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                lastOffset = recyclerView.computeVerticalScrollOffset()
            }
        })
    }

    class AreaHeaderItem(private val plantDataEmptyChecker: () -> Boolean) : RecyctItemBase() {
        override fun create(inflater: LayoutInflater, parent: ViewGroup) =
            object : RecyctViewHolder(inflater, parent, R.layout.item_header_area_detail) {
                private val descriptionText: TextView by id(R.id.descriptionText)
                private val emptyPlantText: TextView by id(R.id.emptyPlantText)
                private val memoText: TextView by id(R.id.memoText)

                override fun bind(data: Any, atIndex: Int) {
                    when (data) {
                        is AreaInfo -> bind(data)
                    }
                }

                private fun bind(areaInfo: AreaInfo) {
                    descriptionText.text = areaInfo.description
                    areaInfo.memo?.let { memoText.text = it }
                        ?: run { memoText.isVisible = false }
                    emptyPlantText.isVisible = plantDataEmptyChecker()
                }
            }
    }

    class PlantInfoItem : RecyctItemBase() {
        override fun create(inflater: LayoutInflater, parent: ViewGroup) =
            object : RecyctViewHolder(inflater, parent, R.layout.item_plant_info) {
                private val coverImage: ImageView by id(R.id.coverImage)
                private val titleText: TextView by id(R.id.titleText)
                private val subtitleText: TextView by id(R.id.subtitleText)

                override fun bind(data: Any, atIndex: Int) {
                    when (data) {
                        is PlantInfo -> bind(data)
                    }
                }

                private fun bind(plantInfo: PlantInfo) {
                    titleText.text = plantInfo.name
                    subtitleText.text = plantInfo.briefDesc
                    if (plantInfo.imageInfoList.isNotEmpty()) {
                        Glide.with(coverImage).load(plantInfo.imageInfoList[0].url).into(coverImage)
                    }
                }
            }

    }
}