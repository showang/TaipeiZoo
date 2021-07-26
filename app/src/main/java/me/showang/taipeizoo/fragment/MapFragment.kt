package me.showang.taipeizoo.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import github.showang.kat.assignN
import me.showang.taipeizoo.databinding.FragmentMapBinding

class MapFragment : Fragment() {

    private var binding: FragmentMapBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapBinding.inflate(layoutInflater).assignN(::binding).root
    }



}