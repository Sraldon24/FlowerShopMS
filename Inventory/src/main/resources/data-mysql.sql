insert into inventories (inventory_identifier, type) values
                                                         ('inv-2001', 'Fresh Flowers'),
                                                         ('inv-2002', 'Artificial Flowers'),
                                                         ('inv-2003', 'Dried Flowers'),
                                                         ('inv-DELETE-ME', 'Test inventory'); -- ✅ Deletable inventory

-- ===================================
-- INSERT FLOWERS (part of the inventory)
-- ===================================

insert into flowers (flower_identifier, inventory_id, flower_name, flower_color, flower_category, flower_status, stock_quantity, price_amount, price_currency, supplier_id) values
                                                                                                                                                                                ('flw-3001', 'inv-2001', 'Red Rose', 'Red', 'Roses', 'AVAILABLE', 100, 5.99, 'CAD', 'sup-1234'),
                                                                                                                                                                                ('flw-3002', 'inv-2001', 'White Lily', 'White', 'Lilies', 'AVAILABLE', 50, 7.99, 'CAD', 'sup-5678'),
                                                                                                                                                                                ('flw-3003', 'inv-2002', 'Tulip', 'Yellow', 'Tulips', 'AVAILABLE', 80, 4.50, 'CAD', 'sup-9999'),
                                                                                                                                                                                ('flw-DELETE-ME', 'inv-2001', 'Test Flower', 'Blue', 'Test Category', 'AVAILABLE', 1, 1.99, 'CAD', 'sup-9999'); -- ✅ Deletable Flower

-- ===================================
-- INSERT FLOWER OPTIONS(part of the inventory)
-- ===================================

insert into flower_options (flower_id, option_name, option_description, option_price) values
                                                                                          (1, 'Vase', 'Glass vase for roses', 4.99),
                                                                                          (2, 'Gift Wrap', 'Premium wrapping with ribbon', 2.99),
                                                                                          (3, 'Luxury Box', 'Premium decorative box', 9.99),
                                                                                          (4, 'DELETE OPTION', 'Test delete option', 0.99); -- ✅ Deletable Flower Option

-- ===================================
-- INSERT SALES
-- ===================================
