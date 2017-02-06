package ma.ensao.egroupe.ws.web.api;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

@RestController
public class CustomerController {
	
	int idGift=10;
	
	@RequestMapping(
            value = "/customers/{id}/{idProduct}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public String buyProduct(@PathVariable int id,@PathVariable int idProduct) {		
		
        try {
	        Connection con=(Connection) DriverManager.getConnection("jdbc:mysql://localhost/Egicum","root","");
	        
			//declaring statement
	        PreparedStatement prep = (PreparedStatement) con.prepareStatement("SELECT * FROM customers where id_customer=?"); 
	        
	        prep.setInt(1, id);
			ResultSet rows = prep.executeQuery();
            rows.next();
            float customerCredit = rows.getFloat("credit");
            int customerPoints = rows.getInt("points");
            
            PreparedStatement prep2 = (PreparedStatement) con.prepareStatement("SELECT price FROM products where id=?"); 
	        
	        prep2.setInt(1, idProduct);
			ResultSet rows2 = prep2.executeQuery();
			rows2.next();
            float productPrice = rows2.getFloat("price");
            
            if(productPrice<=customerCredit){
            	customerCredit-=productPrice;
            	customerPoints += productPrice;
            	//diminuer credit du customer
            	PreparedStatement prep3 = (PreparedStatement) con.prepareStatement("UPDATE customers set credit=? , points=? where id_customer=?"); 
    	        
            	prep3.setFloat(1, customerCredit);
            	prep3.setInt(2, customerPoints);
            	prep3.setInt(3, id);
    			prep3.executeUpdate();
    			// ajouter une entrer fla table ordres
    			PreparedStatement prep4 = (PreparedStatement) con.prepareStatement("INSERT INTO orders(id_produit,id_customer) VALUES(?,?)"); 
            	prep4.setInt(1, idProduct);
            	prep4.setInt(2, id);
    			prep4.executeUpdate();
    			
    			//gift
    			if (customerPoints >200){
    				customerPoints-=200;
        			PreparedStatement prep5 = (PreparedStatement) con.prepareStatement("INSERT INTO orders(id_produit,id_customer) VALUES(?,?)"); 
        			
        			prep5.setInt(1, idGift);
                	prep5.setInt(2, id);
        			prep5.executeUpdate();
        			//diminuer les point apres le gift
        			PreparedStatement prep6 = (PreparedStatement) con.prepareStatement("UPDATE customers set points=? where id_customer=?"); 
        			
        			prep6.setInt(1, customerPoints);
                	prep6.setInt(2, id);
        			prep6.executeUpdate();
        			
        			return "{\"success\" : 1}";
    			}
    			//return klchi mziaan
    			return "{\"success\" : 2}";
            }else{
            	return "{\"success\" : 0}";
            }

			
		}catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return "{\"success\" : 0}";
	}
}
