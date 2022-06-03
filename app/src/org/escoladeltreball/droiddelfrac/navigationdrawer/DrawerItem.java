package org.escoladeltreball.droiddelfrac.navigationdrawer;

/**
 * @author David Llorca Baron <dllorca.baron@gmail.com>
 * @author Jesica Perea Gil <jesspegil@gmail.com>
 * @author Antonio Riquelme Huerta <antonioriquelmeh@gmail.com>
 *
 * This is free software, licensed under the GNU General Public License v3.
 * See http://www.gnu.org/licenses/gpl.html for more information.
 */
public class DrawerItem {
    private String name;
    private int iconId;
    boolean isAvatar;

    public DrawerItem(String name, int iconId) {
        this.name = name;
        this.iconId = iconId;
    }
    
    public DrawerItem(boolean isAvatar){
    	this(null,0);
    	this.isAvatar = isAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }
    
    public boolean isAvatar(){
    	return isAvatar;
    }
}

