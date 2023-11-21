import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.InputStream
import java.io.OutputStream

class BluetoothViewModel : ViewModel() {
    val outputStream: MutableLiveData<OutputStream?> by lazy {
        MutableLiveData<OutputStream?>()
    }

    val inputStream: MutableLiveData<InputStream?> by lazy {
        MutableLiveData<InputStream?>()
    }
}