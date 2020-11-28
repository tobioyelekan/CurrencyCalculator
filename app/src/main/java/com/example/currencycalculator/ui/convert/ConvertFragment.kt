package com.example.currencycalculator.ui.convert

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.currencycalculator.R
import com.example.currencycalculator.ui.dialog.CurrencyDialog
import com.example.currencycalculator.util.loadImage
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.convert_fragment.*

@AndroidEntryPoint
class ConvertFragment : Fragment(R.layout.convert_fragment), CurrencyDialog.DialogListener {

    private val viewModel: ConvertViewModel by viewModels()

    private var typing1 = false
    private var typing2 = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        day30Label.isSelected = true
        day90Label.isSelected = false

        day90.setOnClickListener { toggleDays(DayType.DAY_90) }
        day30.setOnClickListener { toggleDays(DayType.DAY_30) }

        setupObserver()
        setupClickListeners()

        amount1.addTextChangedListener(textWatcher)
        amount2.addTextChangedListener(textWatcher2)

        chart.setViewPortOffsets(0f, 0f, 0f, 0f)
        chart.setBackgroundColor(ResourcesCompat.getColor(resources, R.color.colorBlue, null))

        // enable touch gestures
        chart.setTouchEnabled(true)

        // enable scaling and dragging
        chart.isDragEnabled = false
        chart.setScaleEnabled(true)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(false)

        chart.setDrawGridBackground(false)
        chart.maxHighlightDistance = 300f

        chart.axisRight.isEnabled = false

        val x = chart.xAxis

        x.setLabelCount(6, false)
        x.textColor = Color.WHITE
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(false)
        x.axisLineColor = Color.WHITE
        x.axisLineWidth = 2f
        x.setAxisLineDashedLine(DashPathEffect(floatArrayOf(5f, 80f), 50f))
        x.enableAxisLineDashedLine(5f, 80f, 50f)

        chart.axisLeft.isEnabled = false

        // no description text
        chart.description.isEnabled = false
        chart.legend.isEnabled = false

        chart.animateXY(2000, 2000)

        // don't forget to refresh the drawing
        chart.invalidate()

        setData()

    }

    /**
     * The typing1 and typing2 variable represents a flag for knowing which
     * editext we're typing in to prevent executing onTextChange of the other edit text
     * for ex: while typing on the first edit text, since the other edit text too must be set,
     * we set typing1 = true where appropriate, then in the second edit text we do nothing if typing1 is true,
     * but regardless of the flag, text must still be set.
     */
    private val textWatcher = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            typing1 = false
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            typing1 = true
        }

        override fun onTextChanged(amount: CharSequence, p1: Int, p2: Int, p3: Int) {
            if (!typing2) {
                typing1 = true
                viewModel.convertCurrency(amount.toString(), 0)
            }
        }
    }

    private val textWatcher2 = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
            typing2 = false
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            typing2 = true
        }

        override fun onTextChanged(amount: CharSequence, p1: Int, p2: Int, p3: Int) {
            if (!typing1) {
                typing2 = true
                viewModel.convertCurrency(amount.toString(), 1)
            }
        }
    }

    private fun toggleDays(dayType: DayType) {
        when (dayType) {
            DayType.DAY_30 -> {
                if (!day30Label.isSelected) {
                    day30Label.isSelected = true
                    day30Ind.visibility = View.VISIBLE

                    day90Label.isSelected = false
                    day90Ind.visibility = View.INVISIBLE
                }
            }
            DayType.DAY_90 -> {
                if (!day90Label.isSelected) {
                    day90Label.isSelected = true
                    day90Ind.visibility = View.VISIBLE

                    day30Label.isSelected = false
                    day30Ind.visibility = View.INVISIBLE
                }
            }
        }
    }

    private fun setupClickListeners() {
        currencyContainer1.setOnClickListener { showDialog(0) }
        currencyContainer2.setOnClickListener { showDialog(1) }
    }

    private fun showDialog(pos: Int) {
        val fragmentDialog = CurrencyDialog.getInstance(pos)
        fragmentDialog.show(childFragmentManager, "CurrencyDialog")
    }

    private fun setupObserver() {

        viewModel.firstCurrency.observe(viewLifecycleOwner, Observer {
            it?.let {
                with(it) {
                    currency1.text = currency
                    currencyAbrv1.text = currency
                    currencyLogo1.loadImage(currency)
                }

            }
        })

        viewModel.secondCurrency.observe(viewLifecycleOwner, Observer {
            it?.let {
                with(it) {
                    currency2.text = currency
                    currencyAbrv2.text = currency
                    currencyLogo2.loadImage(currency)
                }

            }
        })

        viewModel.amountText1.observe(viewLifecycleOwner, Observer {
            amount1.setText(it)
        })

        viewModel.amountText2.observe(viewLifecycleOwner, Observer {
            amount2.setText(it)
        })
    }

    /**
     * the idea of assigning false to typing1 and typing2 is to
     * disable onTextChange from executing if after selecting a currency
     */
    override fun onSelectCurrency(pos: Int, currency: String) {
        typing1 = false
        typing2 = false
        viewModel.changeCurrency(pos, currency, amount1.text.toString(), amount2.text.toString())
    }

    //fake implementation
    private fun setData() {
        val entries = mutableListOf<Entry>()
        val date = listOf("01 Jun", "07 Jun", "15 Jun", "23 Jun", "30 Jun")
        for ((index, value) in (0..4).withIndex()) {
            val i = (Math.random() + 20).toFloat()
            entries.add(Entry(value.toFloat(), i, date[index]))
        }

        val dataSet = LineDataSet(entries, "label")
        dataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        dataSet.cubicIntensity = 0.2f
        dataSet.setDrawFilled(true)
        dataSet.setDrawCircles(false)
        dataSet.lineWidth = 0f
        dataSet.circleRadius = 4f
        dataSet.setCircleColor(Color.BLUE)
        dataSet.highLightColor = Color.rgb(244, 117, 117)
        dataSet.color = Color.WHITE
        dataSet.fillColor = Color.WHITE
        dataSet.fillAlpha = 100
        dataSet.setDrawHorizontalHighlightIndicator(false)
        dataSet.fillFormatter =
            IFillFormatter { _, _ -> chart.axisLeft.axisMinimum }

        val lineData = LineData(dataSet)
        lineData.setDrawValues(false)

        // drawables only supported on api level 18 and above
        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.fade_white)
        dataSet.fillDrawable = drawable

        chart.data = lineData
        chart.invalidate()
    }

    enum class DayType {
        DAY_30, DAY_90
    }

}