package zjc.entity.product;

import lombok.Data;
import zjc.entity.base.BaseEntity;

@Data
public class ProductDetails extends BaseEntity {

	private Long productId;
	private String imageUrls;

}