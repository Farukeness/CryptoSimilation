import com.google.gson.annotations.SerializedName

/*
Copyright (c) 2021 Kotlin Data Classes Generated from JSON powered by http://www.json2kotlin.com

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

For support, please feel free to contact me at https://www.linkedin.com/in/syedabsar */


data class Json4Kotlin_Base (

	@SerializedName("id") val id : String,
	@SerializedName("currency") val currency : String,
	@SerializedName("symbol") val symbol : String,
	@SerializedName("name") val name : String,
	@SerializedName("logo_url") val logo_url : String,
	@SerializedName("status") val status : String,
	@SerializedName("price") val price : Double,
	@SerializedName("price_date") val price_date : String,
	@SerializedName("price_timestamp") val price_timestamp : String,
	@SerializedName("circulating_supply") val circulating_supply : Int,
	@SerializedName("max_supply") val max_supply : Int,
	@SerializedName("market_cap") val market_cap : Int,
	@SerializedName("market_cap_dominance") val market_cap_dominance : Double,
	@SerializedName("num_exchanges") val num_exchanges : Int,
	@SerializedName("num_pairs") val num_pairs : Int,
	@SerializedName("num_pairs_unmapped") val num_pairs_unmapped : Int,
	@SerializedName("first_candle") val first_candle : String,
	@SerializedName("first_trade") val first_trade : String,
	@SerializedName("first_order_book") val first_order_book : String,
	@SerializedName("rank") val rank : Int,
	@SerializedName("rank_delta") val rank_delta : Int,
	@SerializedName("high") val high : Double,
	@SerializedName("high_timestamp") val high_timestamp : String,
	//@SerializedName("1d") val 1d : 1d
)