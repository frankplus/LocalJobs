package com.esp.localjobs

import com.esp.localjobs.models.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.fragment_filter_results.*

/**
 * Fragment used to set filter params (longitude, latitude, range, tags)
 * TODO dialog on back button pressed: discard filter options?
 * TODO add place picker
 */
class FilterResultsFragment : Fragment() {
    private val args: FilterResultsFragmentArgs by navArgs()
    private lateinit var rangeEditText: EditText
    private lateinit var queryEditText: EditText
    private lateinit var rangeSeekBar: SeekBar
    private val filterViewModel: FilterViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_filter_results, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rangeEditText = view.findViewById(R.id.range_edit_text)
        queryEditText = view.findViewById(R.id.query_edit_text)
        rangeSeekBar = view.findViewById(R.id.range_seek_bar)

        rangeEditText.setText(filterViewModel.range.value.toString())
        queryEditText.setText(filterViewModel.query.value)
        rangeSeekBar.progress = filterViewModel.range.value ?: -1

        rangeSeekBar.max = 50
        rangeSeekBar.setOnSeekBarChangeListener(seekBarHandler)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            updateViewModel()
            filterViewModel.userRequestedFilteredResults.value = true
            if (args.filteringJobs)
                findNavController().navigate(R.id.action_destination_filter_to_destination_jobs)
            else
                findNavController().navigate(R.id.action_destination_filter_to_destination_proposals)
        }
    }

    private fun updateViewModel() {
        filterViewModel.query.value = queryEditText.text.toString()
        filterViewModel.range.value = rangeEditText.text.toString().toInt()
        filterViewModel.location.value = Location(0.0, 0.0)
    }

    private val seekBarHandler = object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            rangeEditText.setText(progress.toString())
        }
        override fun onStartTrackingTouch(seekBar: SeekBar?) { }
        override fun onStopTrackingTouch(seekBar: SeekBar?) { }
    }

}
