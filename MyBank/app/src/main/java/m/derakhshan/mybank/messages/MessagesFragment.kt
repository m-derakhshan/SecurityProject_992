package m.derakhshan.mybank.messages

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.AuthFailureError
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import m.derakhshan.mybank.Address
import m.derakhshan.mybank.R
import m.derakhshan.mybank.Utils
import m.derakhshan.mybank.databinding.FragmentMessagesBinding
import org.json.JSONObject


class MessagesFragment : Fragment(), MessageListener {

    companion object {
        var showLoading = true
    }

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
        if (showLoading)
            binding.refresh.isRefreshing = true.also {
                showLoading = false
            }

        val request = object :
            JsonArrayRequest(
                Method.GET,
                Address().joinAccount,
                null,
                {
                    binding.refresh.isRefreshing = false

                    //-------------------------(server response)-----------------------//
                    Log.i("Log", "messages response is$it")
                    for (i in 0 until it.length()) {
                        val info = it.getJSONObject(i)
                        val status = info.getString("status").toString() == "pending"
                        if (status)
                            messageList.add(
                                JoinAccountModel(
                                    id = info.getString("id"),
                                    username = info.getString("username"),
                                    accountNumber = info.getString("requested_account"),
                                    status = info.getString("status") == "pending"
                                )
                            )
                    }
                    //-------------------------(server response)-----------------------//
                    myAdapter.submitList(messageList)
                    if (myAdapter.itemCount > 0)
                        binding.noMessage.visibility = View.GONE
                },
                {
                    binding.refresh.isRefreshing = false
                    try {
                        Log.i(
                            "Log",
                            "Error in messages ${
                                String(
                                    it.networkResponse.data,
                                    Charsets.UTF_8
                                )
                            }"
                        )
                    } catch (e: Exception) {
                        Log.i("Log", "Error in messages $it")
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = "Bearer ${Utils(context = requireContext()).accessToken}"

                return params
            }
        }
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
        integrity: Long?,
        confidentiality: Long?,
        req: JoinAccountModel
    ) {
        val data = JSONObject()
        data.put("status", if (accept) "accepted" else "rejected")
        data.put("conf_label", confidentiality?.plus(1))
        data.put("integrity_label", integrity?.plus(1))

        Log.i("Log", "info accept is $data and address:${Address().setStatusJoinAccount(req.id)}")
        val request = object :
            JsonObjectRequest(
                Method.PUT,
                Address().setStatusJoinAccount(req.id),
                data,
                {
                    binding.refresh.isRefreshing = false

                    try {
                        messageList.remove(req)
                        myAdapter.notifyItemRemoved(position)
                        binding.noMessage.visibility = if (myAdapter.itemCount > 0)
                            View.GONE
                        else View.VISIBLE
                    } catch (e: Exception) {
                        Log.i("Log", "error in accept_delete message $e")
                    }

                    //-------------------------(server response)-----------------------//
                    Log.i("Log", "accept_delete response is$it")
                    //-------------------------(server response)-----------------------//
                },
                {
                    binding.refresh.isRefreshing = false
                    try {
                        Log.i(
                            "Log",
                            "Error in accept message ${
                                String(
                                    it.networkResponse.data,
                                    Charsets.UTF_8
                                )
                            }"
                        )
                    } catch (e: Exception) {
                        Log.i("Log", "Error in accept message $it")
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): MutableMap<String, String> {
                val params = HashMap<String, String>()
                params["Authorization"] = "Bearer ${Utils(context = requireContext()).accessToken}"

                return params
            }
        }
        request.retryPolicy = DefaultRetryPolicy(
            30000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )
        Volley.newRequestQueue(requireContext()).add(request)


    }


}