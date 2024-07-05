import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.example.pillsapp.R
import com.example.pillsapp.databinding.FragmentDialogueBinding
import com.example.pillsapp.ui.adapter.ItemAdapter
import com.example.pillsapp.data.Item
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.text.SimpleDateFormat
import java.util.Locale

class MyDialogFragment : DialogFragment() {
    companion object {
        const val TAG = "MyDialogFragment"
    }

    private lateinit var binding: FragmentDialogueBinding
    private lateinit var originalItem: Item
    private var isTimeChanged: Boolean = false
    private var isDateChanged: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDialogueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        originalItem = arguments?.getParcelable<Item>("item") ?: Item("", "", "", "","")

        binding.ETTitle.setText(originalItem.title)
        binding.ETDescription.setText(originalItem.description)
        binding.timeTV.text = originalItem.time
        binding.TVDate.text = originalItem.date

        binding.BTNChangeTime.setOnClickListener {
            showTimePicker()
        }

        binding.BTNChangeDate.setOnClickListener {
            showDatePicker()
        }

        binding.BTNChangeItem.setOnClickListener {
            updateItem()
        }

        binding.BTNAddItem.setOnClickListener {
            addItem()
        }

        binding.close.setOnClickListener {
            dismiss()
        }

        // Add text change listeners to track changes in EditText fields
        binding.ETTitle.addTextChangedListener {
            isTimeChanged = true
        }

        binding.ETDescription.addTextChangedListener {
            isTimeChanged = true
        }
    }

    private fun showTimePicker() {
        val isSystem24hour = is24HourFormat(requireContext())
        val clockFormat = if (isSystem24hour) TimeFormat.CLOCK_24H else TimeFormat.CLOCK_12H
        val picker = MaterialTimePicker.Builder()
            .setTimeFormat(clockFormat)
            .setHour(12)
            .setMinute(0)
            .build()

        picker.show(childFragmentManager, "TimePicker")

        picker.addOnPositiveButtonClickListener {
            val hour = picker.hour
            val minute = picker.minute
            binding.timeTV.text = String.format("%02d:%02d", hour, minute)
            isTimeChanged = true
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                calendar.set(year, month, day)
                updateDateLabel(calendar)
            },
            year, month, dayOfMonth
        )

        datePicker.show()
    }

    private fun updateDateLabel(calendar: Calendar) {
        val dateFormat = SimpleDateFormat("dd--MM-yyyy", Locale.getDefault())
        binding.TVDate.text = dateFormat.format(calendar.time)
        isDateChanged = true
    }

    private fun updateItem() {
        val title = binding.ETTitle.text.toString()
        val description = binding.ETDescription.text.toString()
        val time = binding.timeTV.text.toString()
        val date = binding.TVDate.text.toString()

        if ((title.isNotBlank() || description.isNotBlank() || isDateChanged || isTimeChanged) &&
            (isDateChanged || isTimeChanged)) {
            val updatedItem = Item(originalItem.id, title, description, time, date)
            val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView.adapter as? ItemAdapter
            adapter?.updateItem(updatedItem)

            dismiss()
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addItem() {
        val title = binding.ETTitle.text.toString()
        val description = binding.ETDescription.text.toString()
        val time = binding.timeTV.text.toString()
        val date = binding.TVDate.text.toString()

        if (title.isNotBlank() && description.isNotBlank() && isDateChanged && isTimeChanged) {
            val newItem = Item("", title, description, time, date)

            val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.recyclerView)
            val adapter = recyclerView.adapter as? ItemAdapter
            adapter?.addItem(newItem)

            dismiss()
        } else {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
