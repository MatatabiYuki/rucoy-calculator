package com.helloyanis.rucoycalculator.ui.train

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.helloyanis.rucoycalculator.R
import com.helloyanis.rucoycalculator.databinding.TrainBinding


class TrainFragment : Fragment() {

    private var _binding: TrainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val trainViewModel =
            ViewModelProvider(this).get(TrainViewModel::class.java)

        _binding = TrainBinding.inflate(inflater, container, false)
        binding.root.findViewById<TextView>(R.id.credits).text = "App by helloyanis\nTraining calculation by Mims"
        binding.root.findViewById<TextView>(R.id.str0).text = "No data"
        binding.root.findViewById<TextView>(R.id.str1).text = "No data"
        binding.root.findViewById<TextView>(R.id.str2).text = "No data"
        binding.root.findViewById<TextView>(R.id.str3).text = "No data"
        binding.root.findViewById<TextView>(R.id.str4).text = "No data"
        binding.root.findViewById<TextView>(R.id.str5).text = "No data"
        val editTextBaseLevel = binding.root.findViewById<EditText>(R.id.baselevel)

        // Ajoutez un écouteur de texte à votre EditText
        editTextBaseLevel.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString()!="" && binding.root.findViewById<EditText>(R.id.stat).text.toString()!="" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString()!="") {
                    train()
                }
            }
        })

        val editTextStat = binding.root.findViewById<EditText>(R.id.stat)

        // Ajoutez un écouteur de texte à votre EditText
        editTextStat.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString()!="" && binding.root.findViewById<EditText>(R.id.stat).text.toString()!="" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString()!="") {
                    train()
                }
            }
        })

        val editTextWeaponAtk = binding.root.findViewById<EditText>(R.id.weaponatk)

        // Ajoutez un écouteur de texte à votre EditText
        editTextWeaponAtk.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val userInput = s.toString()
                if (binding.root.findViewById<EditText>(R.id.baselevel).text.toString()!="" && binding.root.findViewById<EditText>(R.id.stat).text.toString()!="" && binding.root.findViewById<EditText>(R.id.weaponatk).text.toString()!="") {
                    train()
                }
            }
        })

        return binding.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun train(){
        val stat1 = binding.root.findViewById<EditText>(R.id.stat).text.toString().toDouble()
        val weaponatk = binding.root.findViewById<EditText>(R.id.weaponatk).text.toString().toDouble()
        val base = binding.root.findViewById<EditText>(R.id.baselevel).text.toString().toDouble()
        val min_raw_damage: Double = Formulas.auto_min_raw_damage_Calc(stat1, weaponatk, base)
        val max_raw_damage: Double = Formulas.auto_max_raw_damage_Calc(stat1, weaponatk, base)
        val max_raw_crit_damage: Double = Formulas.max_raw_crit_damage_Calc(max_raw_damage)
        var accuracy = 0.0
        var str0=""
        var str1=""
        var str2=""
        var str3=""
        var str4=""
        var str5=""

        //An index variable for mobs[]

        //An index variable for mobs[]
        var pos = 0

        //Iterate through loop until you find a mob you can train on with greater than .1749 accuracy

        //Iterate through loop until you find a mob you can train on with greater than .1749 accuracy
        for (x in mobs.size - 1 downTo 0) {
            if (x == 26 || x == 31) {
                continue
            }
            accuracy =
                Formulas.accuracy_Calc(max_raw_crit_damage, max_raw_damage, min_raw_damage, x)
            if (accuracy >= 0.1749) {
                pos = x
                break
            }
        }

        //Calculate average damage which you need for average time to kill

        //Calculate average damage which you need for average time to kill
        val min_damage: Double = Formulas.min_damage_Calc(min_raw_damage, pos)
        val max_damage: Double = Formulas.max_damage_Calc(max_raw_damage, pos)
        val max_crit_damage: Double = Formulas.max_crit_damage_Calc(max_raw_crit_damage, pos)
        val avgdmg: Double =
            Formulas.average_damage_Calc(accuracy, max_damage, min_damage, max_crit_damage)
        val tickrate: Double = Formulas.tickrate_Calc(accuracy, 3600.toDouble())

        //In certain cases you can effective train on two mobs

        //In certain cases you can effective train on two mobs
        var onemob = true
        var checknextmob = true
        var newpos = pos + 1
        if (pos == 5 || pos == 20 || pos == 22 || pos == 28 || pos == 30) {
            pos--
            onemob = false
        }
        if (newpos > 40) {
            checknextmob = false
        }
        if (newpos == 26 || newpos == 31) {
            newpos++
        }

        val time: Double = Formulas.time_to_kill_Calc(avgdmg, pos)
        str0 = """
            ${
            "👾 You can train effectively on " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                .getEmoji_code()*/
        }!
            
            """.trimIndent()
        if (!onemob) {
            val time2: Double = Formulas.time_to_kill_Calc(avgdmg, pos + 1)
            str0 = """
                ${
                "👾 You can train effectively on " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/ + " & " + mobs.get(pos + 1).mob_name /*+ mobs.get(pos + 1)
                    .getEmoji_code()*/
            }!
                
                """.trimIndent()
            str3 = """
                ${
                "⏱️ Average time to kill " + mobs.get(pos + 1).mob_name /*+ mobs.get(pos + 1)
                    .getEmoji_code()*/
            }: ${time2.toInt() / 60} min. ${time2.toInt() % 60} sec.
                
                """.trimIndent() //part of output embed
        }

        //calculating stats you need to train on the next mob

        //calculating stats you need to train on the next mob
        var statadd = 0
        var checked = false
        var alrdealdamage = false
        var dealdamage = false
        var new_max_damage: Double
        var newaccuracy = 0.0
        while (newaccuracy < 0.1749 && checknextmob) {
            val statneeded = stat1 + statadd
            val new_min_raw_damage: Double =
                Formulas.auto_min_raw_damage_Calc(statneeded, weaponatk, base)
            val new_max_raw_damage: Double =
                Formulas.auto_max_raw_damage_Calc(statneeded, weaponatk, base)
            val new_max_raw_critdamage: Double =
                Formulas.max_raw_crit_damage_Calc(new_max_raw_damage)
            new_max_damage = Formulas.max_damage_Calc(new_max_raw_damage, newpos)
            newaccuracy = Formulas.accuracy_Calc(
                new_max_raw_critdamage,
                new_max_raw_damage,
                new_min_raw_damage,
                newpos
            )
            if (new_max_damage >= 1 && !checked) { //if you can already deal damage to the next mob
                str5 =
                    "💥 You can deal " + new_max_damage.toInt() + " max damage to " + mobs.get(
                        newpos
                    ).mob_name /*+ mobs.get(newpos).getEmoji_code()*/ + "!" //part of output
                alrdealdamage = true
            } else if (new_max_damage > 1 && !alrdealdamage && !dealdamage) { //if you cant deal damage to the next mob yet, you can deal damage in a certain amount of stats!
                str5 =
                    "💥 You can deal " + new_max_damage.toInt() + " max damage to " + mobs.get(
                        newpos
                    ).mob_name /*+ mobs.get(newpos)
                        .getEmoji_code()*/ + " in " + statadd + " stats!" //part of output
                dealdamage = true
            }
            checked = true
            statadd++
        }
        if (checknextmob) {
            str1 =
                """${"🔥 Max. Damage: " + max_damage.toInt() + " " /*+ */} Tickrate: ${tickrate.toInt()} / 3600
"""
            str2 = """
                ${
                "⏱️ Average time to kill " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/
            }: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
                
                """.trimIndent()
            str4 = """
                ${
                "💪 You need $statadd stats to train effectively on " + mobs.get(newpos)
                    .mob_name /*+ mobs.get(newpos).getEmoji_code()*/
            }!
                
                """.trimIndent()
        } else {
            str1 =
                """${"⏬ Min. Damage (Auto): " + min_damage.toInt() + " " /*+ slime_lord_emoji*/} ⏫ Max. Damage (Auto): ${max_damage.toInt()}
"""
            str2 = """
                ${
                "⏱️ Average time to kill " + mobs.get(pos).mob_name /*+ mobs.get(pos)
                    .getEmoji_code()*/
            }: ${time.toInt() / 60} min. ${time.toInt() % 60} sec.
                
                """.trimIndent()
        }
        binding.root.findViewById<TextView>(R.id.str0).text = str0
        binding.root.findViewById<TextView>(R.id.str1).text = str1
        binding.root.findViewById<TextView>(R.id.str2).text = str2
        binding.root.findViewById<TextView>(R.id.str3).text = str3
        binding.root.findViewById<TextView>(R.id.str4).text = str4
        binding.root.findViewById<TextView>(R.id.str5).text = str5
    }
}
