package com.anwesh.uiprojects.linkedoppositelinearrowview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.oppositelinearrowview.OppositeLineArrowView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        OppositeLineArrowView.create(this)
    }
}
