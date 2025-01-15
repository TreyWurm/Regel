package at.nukular.regel

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import at.nukular.core.extensions.with
import at.nukular.core.ui.ThemedActivity
import at.nukular.core.ui.onRestoreOnly
import at.nukular.core.ui.viewmodel.log
import at.nukular.regel.databinding.ActivityMainBinding
import at.nukular.regel.model.VHUEntry
import at.nukular.regel.rv.EntryAdapter
import at.nukular.regel.viewmodel.MainUiEvent
import at.nukular.regel.viewmodel.MainUiState
import at.nukular.regel.viewmodel.MainUserAction
import at.nukular.regel.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ThemedActivity(),
    DatePickerDialog.OnDateSetListener {

    @Inject
    lateinit var httpClient: HttpClient

    override val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    private val adapter = EntryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun createViewBindings(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initViews(bundle: Bundle?) {

    }

    override fun initRVs(bundle: Bundle?) {
        binding.rvItems.with {
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = adapter
        }
    }

    override fun initListeners() {
        binding.btnAdd.setOnClickListener {
            val today = LocalDate.now()
            DatePickerDialog(this, this, today.year, today.monthValue - 1, today.dayOfMonth).show()
        }

        binding.cvCalendar.scrollListener = object : at.nukular.core.ui.views.calendar.CalendarListener {
            override fun monthScrolled(month: YearMonth) {
                binding.tvCurrentMonth.text = month.format(DateTimeFormatter.ofPattern("MMMM yyyy"))
            }
        }

        binding.btnToday.setOnClickListener {binding.cvCalendar.scrollToToday()}
    }


    // ========================================================================================================================
    // region ViewModel

    override fun setupObservation() {
        lifecycleScope.launch {
            launch {
                viewModel.uiState
                    .flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                    .distinctUntilChanged()
                    .onRestoreOnly(this@MainActivity)
                    .collect(uiStateObserver)
            }

            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.uiEvents.collect(uiEventObserver) }
            }
        }
    }

    private val uiStateObserver: FlowCollector<MainUiState> = FlowCollector { uiState ->
        uiState.log()
    }

    private val uiEventObserver: FlowCollector<MainUiEvent> = FlowCollector { uiEvent ->
        uiEvent.log()

        when (uiEvent) {
            is MainUiEvent.EntryExists -> entryExistsEventImpl(uiEvent)
            is MainUiEvent.InitialDataLoaded -> initialDataLoadedEventImpl(uiEvent)
            is MainUiEvent.NewEntry -> newEntryEventImpl(uiEvent)
        }
    }

    private fun entryExistsEventImpl(uiEvent: MainUiEvent.EntryExists) {
        Toast.makeText(this, "Date already exists", Toast.LENGTH_SHORT).show()
    }

    private fun initialDataLoadedEventImpl(uiEvent: MainUiEvent.InitialDataLoaded) {
        adapter.setEntries(uiEvent.pastDates.toList())
        binding.cvCalendar.addSelectedDays(uiEvent.pastDates + uiEvent.estimations)
        binding.tvNext.text = Constants.dayOfMonthMonthAbbrYear.format(uiEvent.estimations.first())
    }

    private fun newEntryEventImpl(uiEvent: MainUiEvent.NewEntry) {
        adapter.addEntries(listOf(VHUEntry(uiEvent.date)))
//        binding.tvNext.text = Constants.dayOfMonthMonthAbbrYear.format(.first())
    }
    // endregion


    // ========================================================================================================================
    // region DatePicker

    override fun onDateSet(view: DatePicker, year: Int, month: Int, dayOfMonth: Int) {
        viewModel.onUserAction(MainUserAction.NewDateSelected(LocalDate.of(year, month + 1, dayOfMonth)))
    }
    // endregion
}


