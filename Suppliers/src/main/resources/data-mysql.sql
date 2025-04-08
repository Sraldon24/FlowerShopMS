insert into suppliers (supplier_identifier, company_name, contact_person, email_address, username, password, street_address, city, province, postal_code) values
                                                                                                                                                              ('sup-1234', 'FloraWorld', 'Alice Green', 'alice@floraworld.com', 'aliceg', 'securepass', '123 Bloom St', 'Montreal', 'Quebec', 'H1A 1A1'),
                                                                                                                                                              ('sup-5678', 'Petal Paradise', 'Bob Bloom', 'bob@petalparadise.com', 'bobb', 'securepass', '456 Rose Ave', 'Toronto', 'Ontario', 'M2B 2B2'),
                                                                                                                                                              ('sup-9999', 'NoFlowers Supplier', 'Charlie Green', 'charlie@noflowers.com', 'charlieg', 'securepass', '789 Leaf Rd', 'Vancouver', 'British Columbia', 'V3C 3C3'),
                                                                                                                                                              ('sup-DELETE-ME', 'Unused Supplier', 'Test Person', 'test@unusedsupplier.com', 'testuser', 'testpass', '999 Nowhere St', 'Nowhere', 'Nowhere Province', 'X0X 0X0'); -- ✅ Deletable Supplier

-- ===================================
-- INSERT SUPPLIER PHONE NUMBERS
-- ===================================

insert into supplier_phonenumbers (supplier_id, phone_type, phone_number) values
                                                                              (1, 'MOBILE', '514-123-4567'),
                                                                              (2, 'OFFICE', '416-234-5678'),
                                                                              (3, 'WORK', '604-555-7890'),
                                                                              (4, 'MOBILE', '000-000-0000'); -- ✅ Deletable Phone Number

-- ===================================
-- INSERT EMPLOYEES
-- ===================================
