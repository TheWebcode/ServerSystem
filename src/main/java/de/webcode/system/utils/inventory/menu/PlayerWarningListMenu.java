package de.webcode.system.utils.inventory.menu;

import de.webcode.system.utils.LanguageService;
import de.webcode.system.utils.PlayerManagementService;
import de.webcode.system.utils.inventory.Menu;
import de.webcode.system.utils.inventory.menuutility.TargetPlayerMenuUtility;
import de.webcode.system.utils.reporting.Warning;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class PlayerWarningListMenu extends Menu {

    public PlayerWarningListMenu(TargetPlayerMenuUtility utility){
        super(utility);
    }
    @Override
    public String getMenuName() {
        return "§8Verwarnungen > §6" + ((TargetPlayerMenuUtility) playerMenuUtility).getTarget().getName();
    }

    @Override
    public int getSlots() {
        return 54;
    }

    @Override
    public void handleMenu(InventoryClickEvent e) {
        ItemStack currentItem = e.getCurrentItem();

        if(currentItem.getType() == Material.BARRIER){
            PlayerManagementService.getService().resetWarnings(((TargetPlayerMenuUtility) playerMenuUtility).getTarget());
            e.getWhoClicked().sendMessage(LanguageService.getMessageWithPrefix("command.warn.reset_success").replace("{player}", ((TargetPlayerMenuUtility) playerMenuUtility).getTarget().getName()));
            e.getWhoClicked().closeInventory();
        }
    }

    @Override
    public void setMenuItems() {
        Player target = ((TargetPlayerMenuUtility) playerMenuUtility).getTarget();
        ArrayList<Warning> warnings = PlayerManagementService.getService().getWarnings(target);

        for (int i = 0; i < 10; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        inventory.setItem(17, super.FILLER_GLASS);
        inventory.setItem(18, super.FILLER_GLASS);
        inventory.setItem(26, super.FILLER_GLASS);
        inventory.setItem(27, super.FILLER_GLASS);
        inventory.setItem(35, super.FILLER_GLASS);
        inventory.setItem(36, super.FILLER_GLASS);

        for (int i = 44; i < 54; i++) {
            if (inventory.getItem(i) == null) {
                inventory.setItem(i, super.FILLER_GLASS);
            }
        }

        if(warnings == null) return;

        int count = 1;

        for (Warning warning : warnings) {
            ItemStack itemStack = new ItemStack(Material.PAPER);
            ItemMeta itemMeta = itemStack.getItemMeta();

            String color = "§a";

            switch(count){
                case 1 -> color = "§a";
                case 2 -> color = "§e";
                case 3 -> color = "§c";
            }
            itemMeta.setDisplayName(color + "Verwarnung " + count);

            ArrayList<String> lore = new ArrayList<>();
            lore.add("\n");
            lore.add("§8" + count + " Verwarnung für " + warning.getWarnedPlayer().getName());
            lore.add("\n");
            lore.add("§7Datum der Verwarnung: §6" + warning.getDate());
            lore.add("§7Verwarnt von: §6" + warning.getMod());
            lore.add("§7Verwarnt wegen:\n");
            lore.add("§7" + warning.getReason());

            itemMeta.setLore(lore);
            itemStack.setItemMeta(itemMeta);
            count++;

            inventory.addItem(itemStack);
        }

        ItemStack clearWarnings = makeItem(Material.BARRIER, "§aAlle Verwarnungen löschen", "\n", "§7Alle Verwarnungen für §6" + target.getName() + " §alöschen");

        inventory.setItem(49, clearWarnings);
    }
}
