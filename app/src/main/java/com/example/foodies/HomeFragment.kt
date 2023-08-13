import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.foodies.R
import com.example.foodies.SharedViewModel

class HomeFragment : Fragment() {
    private lateinit var sharedViewModel: SharedViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val rootView = inflater.inflate(R.layout.fragment_home, container, false)
        val viewVendorsButton = rootView.findViewById<Button>(R.id.button_ViewVendors)

        viewVendorsButton.setOnClickListener {
            sharedViewModel.onViewVendorsClick()
        }

        // Inflate the layout for this fragment
        return rootView
    }
}