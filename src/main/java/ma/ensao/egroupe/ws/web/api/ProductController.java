package ma.ensao.egroupe.ws.web.api;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import ma.ensao.egroupe.model.Product;

@RestController
public class ProductController {

	@RequestMapping(
            value = "/products",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Collection<Product>> getProducts() {
		Collection<Product> products = new ArrayList<Product>();
		
        try {
	        Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost/egicum","root","");
	        
			//declaring statement
	        Statement stmt = (Statement) con.createStatement();
			String selectStat="SELECT * FROM products";
			ResultSet rows = stmt.executeQuery(selectStat);
			
            while (rows.next()) {
                int productId = rows.getInt("id");
                String productName = rows.getString("name");
                float productPrice = rows.getFloat("price");
                String productImgSrc = rows.getString("ImgSrc");
                String productDescription = rows.getString("description");
                	
                Product p1=new Product();
        		p1.setName(productName);
        		p1.setId(productId);
        		p1.setPrice(productPrice);
        		p1.setDescription(productDescription);
        		p1.setImgSrc(productImgSrc);
        		
        		products.add(p1);
                }

			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return new ResponseEntity<Collection<Product>>(products,
                HttpStatus.OK);
    }
}
