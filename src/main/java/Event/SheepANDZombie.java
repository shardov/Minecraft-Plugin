package Event;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class SheepANDZombie implements Listener {
    private final JavaPlugin plugin;

    public SheepANDZombie(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onBreackingBlock(BlockBreakEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Material blockType = block.getType();

        // Спавн овцы при разрушении травы или земли
        if (blockType == Material.GRASS_BLOCK || blockType == Material.DIRT) {
            Sheep sheep = (Sheep) block.getWorld().spawn(block.getLocation(), Sheep.class);
            sheep.setColor(DyeColor.PURPLE);
        }

        // Спавн зомби при разрушении камня или булыжника
        if (blockType == Material.STONE || blockType == Material.COBBLESTONE) {
            for (int i = 5; i > 0; i--) {
                int countdown = i;
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.sendMessage("До спавна зомби осталось: " + countdown);
                }, 20 * (5 - countdown));
            }

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                Zombie zombie = block.getWorld().spawn(player.getLocation().add(2, 0, 0), Zombie.class);
                zombie.setCustomName("§eя пидарас");
                zombie.setCustomNameVisible(true);
                equipZombie(zombie); // Экипируем зомби
            }, 100);
        }
    }

    @EventHandler
    public void onZombieDamage(EntityDamageByEntityEvent event) {
        Entity damaged = event.getEntity();
        Entity damager = event.getDamager();

        // Проверяем, что отпиздили  зомби и отпиздил  игрок
        if (damaged instanceof Zombie && damager instanceof Player) {
            Zombie zombie = (Zombie) damaged;
            Player player = (Player) damager;

            // Получаем урон, нанесенный зомби
            double damage = event.getDamage();
            double remainingHealth = zombie.getHealth() - damage;

            // Устанавливаем оставшееся здоровье зомби
            if (remainingHealth < 0) {
                remainingHealth = 0; // Зомби не может иметь отрицательное здоровье
            }
            zombie.setHealth(remainingHealth);

            // Отправляем сообщение игроку
            player.sendMessage("Было наненсено " + damage + " урона зомби. Осталось здоровья: " + remainingHealth);
        }
    }

    private void equipZombie(Zombie zombie) {
        // Выдаем зомби броню
        ItemStack helmet = new ItemStack(Material.GOLDEN_HELMET);
        ItemStack chestplate = new ItemStack(Material.GOLDEN_CHESTPLATE);
        ItemStack leggings = new ItemStack(Material.GOLDEN_LEGGINGS);
        ItemStack boots = new ItemStack(Material.GOLDEN_BOOTS);

        // Одеваем броню на зомби
        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(chestplate);
        zombie.getEquipment().setLeggings(leggings);
        zombie.getEquipment().setBoots(boots);
    }
}