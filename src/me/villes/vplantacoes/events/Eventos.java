package me.villes.vplantacoes.events;

import me.villes.vplantacoes.Main;
import me.villes.vplantacoes.objects.FazendaItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Eventos implements Listener {

    private HashMap<Location, Boolean> temFazenda = new HashMap<>();

    @EventHandler
    public void aoColocar(BlockPlaceEvent e) {
        if (e.getPlayer() instanceof Player) {
            Player player = e.getPlayer();
            ItemStack hand = e.getItemInHand();
            if (hand.hasItemMeta()) {
                if (hand.getItemMeta().hasDisplayName()) {
                    if (hand.getItemMeta().getDisplayName().equals(Main.getInstance().getConfig().getString("item_name").replace("&", "§"))) {
                        Block block = e.getBlock();
                        Location location = block.getLocation();
                        gerarFazenda(player, location);
                    }
                }
            }
        }
    }

    @EventHandler
    public void aoQuebrar(BlockBreakEvent e) {
        if (e.getPlayer() != null) {
            Player player = e.getPlayer();
            Block block = e.getBlock();
            Location location = block.getLocation();
            if(Main.getInstance().fazendas.containsKey(player.getName())) {
                if (Main.getInstance().fazendas.get(player.getName()).contains(location)) {
                    e.setCancelled(true);
                }
                if (block.getType() == Material.MELON_BLOCK) {
                    Location location1 = block.getLocation().add(0, -1, 0);
                    if (Main.getInstance().fazendas.get(player.getName()).contains(location1)) {
                        gerarPlantacao(location1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void aoClicar(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        Location location = block.getLocation();
        if (e.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (Main.getInstance().fazendas.get(player.getName()).contains(location)) {
                if (player.isSneaking()) {
                    Main.getInstance().fazendas.get(player.getName()).remove(location);
                    block.setType(Material.AIR);
                    player.getInventory().addItem(FazendaItem.criarFazenda(1));
                    player.sendMessage("§aFazenda removida com sucesso!");
                    Main.getInstance().saveFazendas();
                } else {
                    e.setCancelled(true);
                    player.sendMessage("§cPara remover uma fazenda, você deve estar no shift.");
                }

            }
        }
    }

    public void gerarPlantacao(Location location) {
        long r = (long) (Math.random() * (50 - 20 + 1) + 20);
        if (temFazenda.get(location)) {
            Block block = location.getBlock().getRelative(BlockFace.UP);
            if (block.getType() == Material.AIR || block.getType().getId() == (Main.getInstance().getConfig().getInt("farm_item"))) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        block.setType(Material.getMaterial(Main.getInstance().getConfig().getInt("farm_item")));
                        block.setData((byte) Main.getInstance().getConfig().getInt("farm_data"));
                    }
                }.runTaskLater(Main.getInstance(), r);
            }
        }
    }

    public void gerarFazenda(Player player, Location location) {
        Block block = location.getBlock();
        block.setType(Material.HAY_BLOCK);
        temFazenda.put(location, true);
        gerarPlantacao(location);
        List<Location> allfazendas = Main.getInstance().fazendas.get(player.getName());
        if (allfazendas != null) {
            allfazendas.add(location);
            Main.getInstance().fazendas.put(player.getName(), allfazendas);
        } else {
            List<Location> allfazendas2 = new ArrayList<>();
            allfazendas2.add(location);
            Main.getInstance().fazendas.put(player.getName(), allfazendas2);
        }
        player.sendMessage("§aFazenda colocada com sucesso!");
        Main.getInstance().saveFazendas();
    }
}
