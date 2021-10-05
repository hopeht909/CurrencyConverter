package com.example.jsonapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import android.view.View
import android.widget.*
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var curencyDetails: Datum? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val userinp = findViewById<View>(R.id.etEuro) as EditText
        val convrt = findViewById<View>(R.id.btConvert) as Button
        val spinner = findViewById<View>(R.id.spinner) as Spinner

        val cur = arrayListOf("inr", "usd", "aud", "sar", "cny", "jpy")

        var selected: Int = 0

        if (spinner != null) {
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_spinner_item, cur
            )
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View, position: Int, id: Long
                ) {
                    selected = position
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }
        convrt.setOnClickListener {

            var sel = userinp.text.toString()
            var currency: Double = sel.toDouble()

            getCurrency(onResult = {
                curencyDetails = it

                when (selected) {
                    0 -> disp(calc(curencyDetails?.eur?.inr?.toDouble(), currency));
                    1 -> disp(calc(curencyDetails?.eur?.usd?.toDouble(), currency));
                    2 -> disp(calc(curencyDetails?.eur?.aud?.toDouble(), currency));
                    3 -> disp(calc(curencyDetails?.eur?.sar?.toDouble(), currency));
                    4 -> disp(calc(curencyDetails?.eur?.cny?.toDouble(), currency));
                    5 -> disp(calc(curencyDetails?.eur?.jpy?.toDouble(), currency));
                }
            })
        }

    }

    private fun disp(calc: Double) {

        val responseText = findViewById<View>(R.id.tvResult) as TextView

        responseText.text = "result " + calc
    }

    private fun calc(i: Double?, sel: Double): Double {
        var s = 0.0
        if (i != null) {
            s = (i * sel)
        }
        return s
    }

    private fun getCurrency(onResult: (Datum?) -> Unit) {
        val apiInterface = APIClient().getClient()?.create(APIInterface::class.java)

        if (apiInterface != null) {
            apiInterface.getCurrency()?.enqueue(object : Callback<Datum> {
                override fun onResponse(
                    call: Call<Datum>,
                    response: Response<Datum>
                ) {
                    onResult(response.body())

                }

                override fun onFailure(call: Call<Datum>, t: Throwable) {
                    onResult(null)
                    Toast.makeText(applicationContext, "" + t.message, Toast.LENGTH_SHORT).show();
                }

            })
        }

    }
}

