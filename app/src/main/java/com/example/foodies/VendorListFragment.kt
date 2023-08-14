import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.foodies.R
import com.example.foodies.SharedViewModel

class VendorListFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vendor_list, container, false)
    }

    // this adds additional functionality to the elements of the XML (fragment_vendor_list)
    // once it has been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backToHomeButton = view.findViewById<Button>(R.id.backToHome_button)
        backToHomeButton.setOnClickListener{

        }
    }
}