package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.databinding.FragmentHomeBinding

class HomeFragment: Fragment() {

    private lateinit var viewBinding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        return super.onCreateView(inflater, container, savedInstanceState)


        viewBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewBinding.textHome.text = "Hello World from Home Fragment"
    }

}