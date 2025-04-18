package com.ex.quizapplication.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ex.quizapplication.databinding.FragmentTopicBinding
import com.ex.quizapplication.model.Category
import com.ex.quizapplication.model.Quizze
import com.ex.quizapplication.view.adapters.TopicAdapter
import com.ex.quizapplication.viewmodel.UserViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TopicFragment : Fragment() {

    private lateinit var binding: FragmentTopicBinding
    private val topicViewModel: UserViewModel by viewModels()
    private var category: Category?= null
    private var topicId: Int = -1
    private var quiz: Quizze? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        category = arguments?.getParcelable("categoryObj") as? Category
        Log.d("TopicFragment", "Category ID: ${category?.id}")

        category?.id?.let { topicViewModel.getTopicsByCategory(it) }
            ?: Log.e("TopicFragment", "Invalid category ID received")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTopicBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.categoryName.text = category?.categoryName

        topicViewModel.topics.observe(viewLifecycleOwner) { topicList ->
            if (topicList.isNotEmpty()) {
                binding.topicRCV.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                val adapter = TopicAdapter(topicList) { topicId ->
                   val action = TopicFragmentDirections.actionTopicFragmentToLiveQuizDetail(quiz, topicId)
                    findNavController().navigate(action)
                }
                binding.topicRCV.adapter = adapter
            } else {
                Log.d("TopicFragment", "No topics found for categoryId: ${category?.categoryName}")
            }
        }
    }
}