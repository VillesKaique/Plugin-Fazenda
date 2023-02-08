package me.villes.vplantacoes;

import me.villes.vplantacoes.commands.Comandos;
import me.villes.vplantacoes.events.Eventos;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main extends JavaPlugin {
    private static Main instance;
    public Map<String, List<Location>> fazendas = new HashMap<>();
    private File locations = null;
    private FileConfiguration locationConfiguration = null;

    @Override
    public void onEnable() {
        getServer().getConsoleSender().sendMessage("==================================================");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "O plugin " + ChatColor.YELLOW + "vPlantacoes" + ChatColor.GREEN + " foi iniciado");
        getServer().getConsoleSender().sendMessage("==================================================");
        getServer().getPluginManager().registerEvents(new Eventos(),this);
        getCommand("fazenda").setExecutor(new Comandos());
        File locationVerification = new File(getDataFolder(), "FazendasLocales.yml");
        if (!locationVerification.exists()) {
            saveResource("FazendasLocales.yml", false);
        }
        loadFazendas();
        saveDefaultConfig();
        instance = this;
    }

    @Override
    public void onDisable() {
        saveFazendas();
    }

    public static Main getInstance() {
        return instance;
    }
    public void loadFazendas(){
        for(String player : getLocaleFile().getKeys(false)){
            fazendas.put(player,(List<Location>)getLocaleFile().get(player));
        }

    }
    public void saveFazendas(){
        for(String player : fazendas.keySet()){
            getLocaleFile().set(player,fazendas.get(player));
            saveLocale();
            reloadLocale();
        }
    }


    public FileConfiguration getLocaleFile(){
        if(this.locationConfiguration == null){
            this.locations = new File(getDataFolder(), "FazendasLocales.yml");
            this.locationConfiguration = YamlConfiguration.loadConfiguration(this.locations);
        }
        return this.locationConfiguration;
    }
    public void saveLocale(){
        try{
            getLocaleFile().save(this.locations);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void reloadLocale(){
        if(this.locations == null){
            this.locations = new File(getDataFolder(), "FazendasLocales.yml");
        }
        this.locationConfiguration = YamlConfiguration.loadConfiguration(this.locations);
        if(this.locationConfiguration != null){
            YamlConfiguration defaults = YamlConfiguration.loadConfiguration(this.locations);
            this.locationConfiguration.setDefaults(defaults);
        }
    }

}
