package m.derakhshan.mybank.messages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import m.derakhshan.mybank.Address
import m.derakhshan.mybank.R
import m.derakhshan.mybank.databinding.FragmentMessagesBinding
import org.json.JSONObject


class MessagesFragment : Fragment(), MessageListener {

    var counter = 0
    private lateinit var binding: FragmentMessagesBinding
    private val myAdapter = MessageRecyclerViewAdapter()
    private val messageList = ArrayList<JoinAccountModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_messages, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        myAdapter.listener = this
        binding.recyclerView.apply {
            this.layoutManager = LinearLayoutManager(requireContext())
            this.adapter = myAdapter
        }
        binding.refresh.setOnRefreshListener {
            getMessages()
        }
        getMessages()
    }


    private fun getMessages() {


        //-------------------------(remove this line and get it from server)-----------------------//
        messageList.add(
            JoinAccountModel(
                id = "${counter++}",
                username = "mohammad",
                "9526603"
            )
        )
        myAdapter.submitList(messageList)
        //-------------------------(remove this line and get it from server)-----------------------//


        val info = JSONObject()

        val request =
            JsonObjectRequest(
                Request.Method.POST,
                "",
                info,
                {
                    binding.refresh.isRefreshing = false

                    //-------------------------(server response)-----------------------//


                    //-------------------------(server response)-----------------------//

                    if (myAdapter.itemCount > 0)
                        binding.noMessage.visibility = View.GONE
                },
                {
                    binding.refresh.isRefreshing = false
                    try {
                        Log.i(
                            "Log",
                            "Error in LoginViewModel_login ${
                                String(
                                    it.networkResponse.data,
                                    Charsets.UTF_8
                                )
                            }"
                        )
                    } catch (e: Exception) {
                        Log.i("Log", "Error in LoginViewModel_Login $it")
                    }
                })
        request.retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(requireContext()).add(request)


    }


    override fun onClickListener(
        position: Int,
        accept: Boolean,
        integrity: String?,
        confidentiality: String?,
        req: JoinAccountModel
    ) {
        if (accept) {
            try {
                messageList.remove(req)
                myAdapter.notifyItemRemoved(position)
                Log.i("Log", "list is$messageList \n id: $req \nposition$position")

            } catch (e: Exception) {
                Log.i("Log", "error in deleting message $e")
            }

        }

        binding.noMessage.visibility = if (myAdapter.itemCount > 0)
            View.GONE
        else View.VISIBLE
    }


}