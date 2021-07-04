package id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.repositories

import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.apis.SaleApi
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.ApiResponse
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.Sale
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.SaleDetail
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.CreateSaleParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.GetSaleDetailParam
import id.turbin.sahabatgemarikan.sahabatgemarikanposmobile.models.repositorymodels.GetSalesParam
import javax.inject.Inject

class SaleRepository @Inject constructor(
    private val saleApi: SaleApi
) {
    suspend fun getSales(getSalesParam: GetSalesParam): ApiResponse<List<Sale>> {
        val token = "Bearer ${getSalesParam.token}"
        return saleApi.getSales(token = token, date = getSalesParam.date)
    }

    suspend fun getSaleDetail(getSaleDetailParam: GetSaleDetailParam): ApiResponse<SaleDetail> {
        val token = "Bearer ${getSaleDetailParam.token}"
        return saleApi.getSaleDetail(token = token, saleId = getSaleDetailParam.saleId)
    }

    suspend fun createSale(createSaleParam: CreateSaleParam): ApiResponse<SaleDetail> {
        val token = "Bearer ${createSaleParam.token}"
        return saleApi.createSale(token = token, createSaleRequest = createSaleParam.createSaleRequest)
    }
}