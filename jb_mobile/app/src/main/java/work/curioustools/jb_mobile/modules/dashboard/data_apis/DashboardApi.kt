package work.curioustools.jb_mobile.modules.dashboard.data_apis

import retrofit2.Call
import retrofit2.http.GET

interface DashboardApi {

    @GET(BOOKLIST)
    fun getBookList() : Call<List<BookItemDto>>

    companion object{
        const val  BASE_URL = "http://192.168.0.30:3000/"

        private const val API = "api"
        const val BOOKLIST = "$API/booklist"
    }
}



