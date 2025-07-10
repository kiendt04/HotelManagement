/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Control;

import DAO.FloorDAO;
import Model.Floor;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class FloorControl {
    
    private FloorDAO flc = new FloorDAO();
    
    public FloorControl() {
    }
    
    public int countFloor()
    {
        return flc.countFloor();
    }
    
    public List<Floor> getAll()
    {
        return flc.getAll();
    }
    
    public int createNewID()
    {
        return flc.creatNewID();
    }
    
    public int insertFloor(Floor fl)
    {
        return flc.insertFloor(fl);
    }
    
    public int uptFloor(Floor fl)
    {
        return flc.uptFloor(fl);
    }
    
    public int delFloor(int id)
    {
        return flc.delFloor(id);
    }
}
