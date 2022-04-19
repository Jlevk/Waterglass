package by.Jlevk

import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class DataModel: ViewModel() {
    val weightValue: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val progress: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val percent: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val glass: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val heightValue: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val index: MutableLiveData<Int> by lazy {
        MutableLiveData<Int>()
    }
    val indexRate: MutableLiveData<TextView> by lazy {
        MutableLiveData<TextView>()
    }

}