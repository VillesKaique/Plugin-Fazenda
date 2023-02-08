package me.villes.vplantacoes.commands;

import me.villes.vplantacoes.objects.FazendaItem;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Comandos implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (cmd.getName().equalsIgnoreCase("fazenda")) {
            if (args.length == 0) {
                cmdCorreto(sender);
            } else {
                if (args[0].equalsIgnoreCase("give")) {
                    if (!(args.length == 1)) {
                        if (!(Bukkit.getPlayer(args[1]) == null)) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (!(args.length == 2)) {
                                if (isInteger(args[2])) {
                                    ItemStack fazendaItem = FazendaItem.criarFazenda(Integer.valueOf(args[2]));
                                    target.getInventory().addItem(fazendaItem);
                                } else {
                                    if (sender instanceof Player) {
                                        Player p = (Player) sender;
                                        p.sendMessage("§c<quantia> tem que ser um numero!");
                                    } else {
                                        sender.sendMessage("§c<quantia> tem que ser um numero!");
                                    }
                                    return true;
                                }
                            } else {
                                cmdCorreto(sender);
                            }
                        } else {
                            if (sender instanceof Player) {
                                Player p = (Player) sender;
                                p.sendMessage("§cJogador não encontrado!");
                            } else {
                                sender.sendMessage("§cJogador não encontrado!");
                            }
                            return true;
                        }
                    } else {
                        cmdCorreto(sender);
                    }
                } else {
                    cmdCorreto(sender);
                }
            }
        }
        return false;
    }

    private boolean cmdCorreto(CommandSender sender) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage("§cUse /fazenda give <Player> <quantia>");
        } else {
            sender.sendMessage("§cUse /fazenda give <Player> <quantia>");
        }
        return true;
    }

    private static boolean isInteger(String str) {
        return str != null && str.matches("[0-9]*");
    }
}
