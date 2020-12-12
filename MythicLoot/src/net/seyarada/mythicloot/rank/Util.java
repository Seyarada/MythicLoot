package net.seyarada.mythicloot.rank;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class Util {
	

	public void ColorHandler(Item item, String color) {
		item.setGlowing(true);
		Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
	
		if(item.getItemStack().getItemMeta().getDisplayName().startsWith("§0"))
			color = "BLACK";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§1"))
			color = "DARK_BLUE";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§2"))
			color = "DARK_GREEN";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§3"))
			color = "DARK_AQUA";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§4"))
			color = "DARK_RED";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§5"))
			color = "DARK_PURPLE";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§6"))
			color = "GOLD";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§7"))
			color = "GRAY";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§8"))
			color = "DARK_GRAY";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§9"))
			color = "BLUE";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§a"))
			color = "GREEN";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§b"))
			color = "AQUA";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§c"))
			color = "RED";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§d"))
			color = "LIGHT_PURPLE";
		else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§e"))
			color = "YELLOW";
		
	    switch(color.toUpperCase()) {
	      
	      case "YELLOW":
	        if(board.getTeam("mLYELLOW")==null) {
	          Team YELLOW = board.registerNewTeam("mLYELLOW");
	          YELLOW.setColor(ChatColor.YELLOW);
	          YELLOW.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLYELLOW").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "LIGHT_PURPLE":
	        if(board.getTeam("mLLIGHT_PURPLE")==null) {
	          Team LIGHT_PURPLE = board.registerNewTeam("mLLIGHT_PURPLE");
	          LIGHT_PURPLE.setColor(ChatColor.LIGHT_PURPLE);
	          LIGHT_PURPLE.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLLIGHT_PURPLE").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "GREEN":
	        if(board.getTeam("mLGREEN")==null) {
	          Team GREEN = board.registerNewTeam("mLGREEN");
	          GREEN.setColor(ChatColor.GREEN);
	          GREEN.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLGREEN").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "GOLD":
	        if(board.getTeam("mLGOLD")==null) {
	          Team GOLD = board.registerNewTeam("mLGOLD");
	          GOLD.setColor(ChatColor.GOLD);
	          GOLD.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLGOLD").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "DARK_RED":
	        if(board.getTeam("mLDARK_RED")==null) {
	          Team DARK_RED = board.registerNewTeam("mLDARK_RED");
	          DARK_RED.setColor(ChatColor.DARK_RED);
	          DARK_RED.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLDARK_RED").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "DARK_PURPLE":
	        if(board.getTeam("mLDARK_PURPLE")==null) {
	          Team DARK_PURPLE = board.registerNewTeam("mLDARK_PURPLE");
	          DARK_PURPLE.setColor(ChatColor.DARK_PURPLE);
	          DARK_PURPLE.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLDARK_PURPLE").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "DARK_GREEN":
	        if(board.getTeam("mLDARK_GREEN")==null) {
	          Team DARK_GREEN = board.registerNewTeam("mLDARK_GREEN");
	          DARK_GREEN.setColor(ChatColor.DARK_GREEN);
	          DARK_GREEN.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLDARK_GREEN").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "DARK_GRAY":
	        if(board.getTeam("mLDARK_GRAY")==null) {
	          Team DARK_GRAY = board.registerNewTeam("mLDARK_GRAY");
	          DARK_GRAY.setColor(ChatColor.DARK_GRAY);
	          DARK_GRAY.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLDARK_GRAY").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "DARK_BLUE":
	        if(board.getTeam("mLDARK_BLUE")==null) {
	          Team DARK_BLUE = board.registerNewTeam("mLDARK_BLUE");
	          DARK_BLUE.setColor(ChatColor.DARK_BLUE);
	          DARK_BLUE.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLDARK_BLUE").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "DARK_AQUA":
	        if(board.getTeam("mLDARK_AQUA")==null) {
	          Team DARK_AQUA = board.registerNewTeam("mLDARK_AQUA");
	          DARK_AQUA.setColor(ChatColor.DARK_AQUA);
	          DARK_AQUA.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLDARK_AQUA").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "BLUE":
	        if(board.getTeam("mLBLUE")==null) {
	          Team BLUE = board.registerNewTeam("mLBLUE");
	          BLUE.setColor(ChatColor.BLUE);
	          BLUE.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLBLUE").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "BLACK":
	        if(board.getTeam("mLBLACK")==null) {
	          Team BLACK = board.registerNewTeam("mLBLACK");
	          BLACK.setColor(ChatColor.BLACK);
	          BLACK.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLBLACK").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "AQUA":
	        if(board.getTeam("mLAQUA")==null) {
	          Team AQUA = board.registerNewTeam("mLAQUA");
	          AQUA.setColor(ChatColor.AQUA);
	          AQUA.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLAQUA").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    
	      case "RED":
	        if(board.getTeam("mLRED")==null) {
	          Team RED = board.registerNewTeam("mLRED");
	          RED.setColor(ChatColor.RED);
	          RED.addEntry(item.getUniqueId().toString());
	        } else {
	          board.getTeam("mLRED").addEntry(item.getUniqueId().toString());
	        }
	        break;
	    }
		
		
		
	}
	
	public String getColor(Item item, String color) {
		
		if(color.equals("display")) {
			if(item.getItemStack().getItemMeta().getDisplayName().startsWith("§0"))
				return "BLACK";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§1"))
				return "DARK_BLUE";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§2"))
				return "DARK_GREEN";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§3"))
				return "DARK_AQUA";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§4"))
				return "DARK_RED";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§5"))
				return "DARK_PURPLE";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§6"))
				return "GOLD";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§7"))
				return "GRAY";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§8"))
				return "DARK_GRAY";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§9"))
				return "BLUE";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§a"))
				return "GREEN";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§b"))
				return "AQUA";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§c"))
				return "RED";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§d"))
				return "LIGHT_PURPLE";
			else if (item.getItemStack().getItemMeta().getDisplayName().startsWith("§e"))
				return "YELLOW";
		} else {
			return color.toUpperCase();
		}
		return null;
	}
	
	public Color getRGB(String color) {
		if(color==null) return Color.fromRGB(255, 255, 255);
		switch(color) {
	      
	      case "YELLOW":
	    	  return Color.fromRGB(255, 255, 85);
	    
	      case "LIGHT_PURPLE":
	    	  return Color.fromRGB(255, 85, 255);
	    
	      case "GREEN":
	    	  return Color.fromRGB(85, 255, 85);
	    
	      case "GOLD":
	    	  return Color.fromRGB(255, 170, 0);
	    
	      case "DARK_RED":
	    	  return Color.fromRGB(170, 0, 0);
	    	  
	      case "GRAY":
	    	  return Color.fromRGB(128,128,128);
	    
	      case "DARK_PURPLE":
	    	  return Color.fromRGB(170, 0, 170);
	    
	      case "DARK_GREEN":
	    	  return Color.fromRGB(0, 170, 0);
	    
	      case "DARK_GRAY":
	    	  return Color.fromRGB(85, 85, 85);
	    
	      case "DARK_BLUE":
	    	  return Color.fromRGB(0, 0, 170);
	    
	      case "DARK_AQUA":
	    	  return Color.fromRGB(0, 170, 170);
	    
	      case "BLUE":
	    	  return Color.fromRGB(85, 85, 255);
	    
	      case "BLACK":
	    	  return Color.fromRGB(0, 0, 0);
	    
	      case "AQUA":
	    	  return Color.fromRGB(85, 255, 255);
	    
	      case "RED":
	    	  return Color.fromRGB(255, 85, 85);
	    }
		return Color.fromRGB(255, 255, 255);
	}
	
	public void msg(Player player, String message) {
		sendCenteredMessage(player, message);
	}
	
	public void sendCenteredMessage(Player player, String message){
		if(player==null) return;
        if(message == null || message.equals(""))
        	player.sendMessage("");

                message = ChatColor.translateAlternateColorCodes('&', message);
               
                int messagePxSize = 0;
                boolean previousCode = false;
                boolean isBold = false;
               
                for(char c : message.toCharArray()){
                        if(c == '§'){
                                previousCode = true;
                                continue;
                        }else if(previousCode == true){
                                previousCode = false;
                                if(c == 'l' || c == 'L'){
                                        isBold = true;
                                        continue;
                                }else isBold = false;
                        }else{
                                DefaultFontInfo dFI = DefaultFontInfo.getDefaultFontInfo(c);
                                messagePxSize += isBold ? dFI.getBoldLength() : dFI.getLength();
                                messagePxSize++;
                        }
                }
               
                int halvedMessageSize = messagePxSize / 2;
                int toCompensate = 154 - halvedMessageSize;
                int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
                int compensated = 0;
                StringBuilder sb = new StringBuilder();
                while(compensated < toCompensate){
                        sb.append(" ");
                        compensated += spaceLength;
                }
                if(player!=null&&sb!=null&&message!=null) {
                	player.sendMessage(sb.toString() + message);
                }
        }
	
	public enum DefaultFontInfo{
		 
		A('A', 5),
		a('a', 5),
		B('B', 5),
		b('b', 5),
		C('C', 5),
		c('c', 5),
		D('D', 5),
		d('d', 5),
		E('E', 5),
		e('e', 5),
		F('F', 5),
		f('f', 4),
		G('G', 5),
		g('g', 5),
		H('H', 5),
		h('h', 5),
		I('I', 3),
		i('i', 1),
		J('J', 5),
		j('j', 5),
		K('K', 5),
		k('k', 4),
		L('L', 5),
		l('l', 1),
		M('M', 5),
		m('m', 5),
		N('N', 5),
		n('n', 5),
		O('O', 5),
		o('o', 5),
		P('P', 5),
		p('p', 5),
		Q('Q', 5),
		q('q', 5),
		R('R', 5),
		r('r', 5),
		S('S', 5),
		s('s', 5),
		T('T', 5),
		t('t', 4),
		U('U', 5),
		u('u', 5),
		V('V', 5),
		v('v', 5),
		W('W', 5),
		w('w', 5),
		X('X', 5),
		x('x', 5),
		Y('Y', 5),
		y('y', 5),
		Z('Z', 5),
		z('z', 5),
		NUM_1('1', 5),
		NUM_2('2', 5),
		NUM_3('3', 5),
		NUM_4('4', 5),
		NUM_5('5', 5),
		NUM_6('6', 5),
		NUM_7('7', 5),
		NUM_8('8', 5),
		NUM_9('9', 5),
		NUM_0('0', 5),
		EXCLAMATION_POINT('!', 1),
		AT_SYMBOL('@', 6),
		NUM_SIGN('#', 5),
		DOLLAR_SIGN('$', 5),
		PERCENT('%', 5),
		UP_ARROW('^', 5),
		AMPERSAND('&', 5),
		ASTERISK('*', 5),
		LEFT_PARENTHESIS('(', 4),
		RIGHT_PERENTHESIS(')', 4),
		MINUS('-', 5),
		UNDERSCORE('_', 5),
		PLUS_SIGN('+', 5),
		EQUALS_SIGN('=', 5),
		LEFT_CURL_BRACE('{', 4),
		RIGHT_CURL_BRACE('}', 4),
		LEFT_BRACKET('[', 3),
		RIGHT_BRACKET(']', 3),
		COLON(':', 1),
		SEMI_COLON(';', 1),
		DOUBLE_QUOTE('"', 3),
		SINGLE_QUOTE('\'', 1),
		LEFT_ARROW('<', 4),
		RIGHT_ARROW('>', 4),
		QUESTION_MARK('?', 5),
		SLASH('/', 5),
		BACK_SLASH('\\', 5),
		LINE('|', 1),
		TILDE('~', 5),
		TICK('`', 2),
		PERIOD('.', 1),
		COMMA(',', 1),
		SPACE(' ', 3),
		DEFAULT('a', 4);
	 
		private final char character;
		private final int length;
	 
		DefaultFontInfo(char character, int length) {
			this.character = character;
			this.length = length;
		}
	 
		public char getCharacter(){
			return this.character;
		}
	 
		public int getLength(){
			return this.length;
		}
	 
		public int getBoldLength(){
			if(this == DefaultFontInfo.SPACE) return this.getLength();
			return this.length + 1;
		}
	 
		public static DefaultFontInfo getDefaultFontInfo(char c){
			for(DefaultFontInfo dFI : DefaultFontInfo.values()){
				if(dFI.getCharacter() == c) return dFI;
			}
			return DefaultFontInfo.DEFAULT;
		}
	}
	
}
