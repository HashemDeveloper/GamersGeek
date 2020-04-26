package com.project.gamersgeek.views.widgets

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.project.gamersgeek.R
import com.project.gamersgeek.models.games.Store
import com.project.gamersgeek.views.recycler.BottomSheetShopListAdapter
import com.project.gamersgeek.views.recycler.items.StoreListAdapter
import kotlinx.android.synthetic.main.bottom_sheet_shop_list_layout.*

class GamersGeekBottomSheet constructor(private val data: List<Store>): BottomSheetDialogFragment(), StoreListAdapter.StoreListClickListener {
    private var shopListAdapter: BottomSheetShopListAdapter?= null
    private val clickLiveDataObserver: MutableLiveData<String> = MutableLiveData()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       return inflater.inflate(R.layout.bottom_sheet_shop_list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val title = "Available On:"
        bottom_sheet_shop_list_title_view_id.text = title
        this.shopListAdapter = BottomSheetShopListAdapter(this)
        bottom_sheet_shop_list_recycler_view_id.layoutManager = LinearLayoutManager(this.context)
        bottom_sheet_shop_list_recycler_view_id.adapter = this.shopListAdapter
        this.shopListAdapter?.setupStoreList(this.data)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return BottomSheetDialog(requireContext(), theme)
    }

    override fun getTheme(): Int {
        return R.style.BaseBottomSheetDialog
    }

    fun getClickObserver(): LiveData<String> {
        return this.clickLiveDataObserver
    }
    override fun onStoreClicked(url: String) {
        this.clickLiveDataObserver.value = url
    }
}