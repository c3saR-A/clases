package consumo.worpress.clases.services;


import consumo.worpress.clases.dto.CreateProductRequest;
import consumo.worpress.clases.dto.WooProductDto;

public interface WooCommerceService {

    WooProductDto createProduct(CreateProductRequest request);
}
