package consumo.worpress.clases.controller;


import consumo.worpress.clases.dto.ApiResponse;
import consumo.worpress.clases.dto.CreateProductRequest;
import consumo.worpress.clases.dto.WooProductDto;
import consumo.worpress.clases.services.WooCommerceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/posts")
public class WooCommerceController {

    private final WooCommerceService wooCommerceService;

    public WooCommerceController(WooCommerceService wooCommerceService){
        this.wooCommerceService = wooCommerceService;
    }

    @PostMapping("/products")
    public ResponseEntity<ApiResponse<WooProductDto>> createProduct(@RequestBody CreateProductRequest request){
        WooProductDto product = wooCommerceService.createProduct(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Producto creado en WooCommerce", product));
    }

}
