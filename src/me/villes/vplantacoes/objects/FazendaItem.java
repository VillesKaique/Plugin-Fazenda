package me.villes.vplantacoes.objects;

import me.villes.vplantacoes.Main;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class FazendaItem {
    public static ItemStack criarFazenda(Integer integer){
        ItemStack fazenda = new ItemStack(Main.getInstance().getConfig().getInt("item_id"),integer);
        ItemMeta fazendameta = fazenda.getItemMeta();
        fazendameta.setDisplayName(Main.getInstance().getConfig().getString("item_name").replace("&","§"));
        ArrayList<String> lore = new ArrayList<>();
        for(String str : Main.getInstance().getConfig().getStringList("item_lore")){
            str = str.replace("&","§");
            lore.add(str);
        }
        fazendameta.setLore(lore);
        fazenda.setItemMeta(fazendameta);
        return fazenda;
    }
}
