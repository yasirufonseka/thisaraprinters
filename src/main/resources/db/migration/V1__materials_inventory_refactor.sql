-- Migration: Refactor Materials and Inventory Domain
-- Description: Split materials into family + variants, add stock lots tracking

-- Create material_variants table
CREATE TABLE IF NOT EXISTS material_variants (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    material_id BIGINT NOT NULL,
    gsm INT,
    width_mm DECIMAL(8,2),
    height_mm DECIMAL(8,2),
    sheets_per_ream INT DEFAULT 500,
    weight_per_unit_kg DECIMAL(8,3),
    unit VARCHAR(50) NOT NULL,
    reorderlevel INT NOT NULL DEFAULT 0,
    status VARCHAR(50) NOT NULL DEFAULT 'ACTIVE',
    FOREIGN KEY (material_id) REFERENCES materials(id) ON DELETE CASCADE,
    INDEX idx_material_id (material_id)
);

-- Alter materials table: drop fields, keep only family-level attributes
ALTER TABLE materials
DROP COLUMN IF EXISTS availablequantity,
DROP COLUMN IF EXISTS units,
DROP COLUMN IF EXISTS reorderlevel,
CHANGE COLUMN material name VARCHAR(255);

-- Create stock_lots table
CREATE TABLE IF NOT EXISTS stock_lots (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    variant_id BIGINT NOT NULL,
    inventory_id BIGINT,
    lot_type VARCHAR(50) NOT NULL,
    quantity DECIMAL(10,2) NOT NULL,
    unit VARCHAR(50) NOT NULL,
    width_mm DECIMAL(8,2),
    height_mm DECIMAL(8,2),
    weight_kg DECIMAL(8,3),
    status VARCHAR(50) NOT NULL DEFAULT 'AVAILABLE',
    source_ref VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (variant_id) REFERENCES material_variants(id) ON DELETE CASCADE,
    FOREIGN KEY (inventory_id) REFERENCES inventory(id) ON DELETE SET NULL,
    INDEX idx_variant_id (variant_id),
    INDEX idx_status (status),
    INDEX idx_lot_type (lot_type)
);

-- Create job_material_usage table
CREATE TABLE IF NOT EXISTS job_material_usage (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    stock_lot_id BIGINT NOT NULL,
    quantity_used DECIMAL(10,2) NOT NULL,
    quantity_returned DECIMAL(10,2) DEFAULT 0,
    FOREIGN KEY (stock_lot_id) REFERENCES stock_lots(id) ON DELETE CASCADE,
    FOREIGN KEY (job_id) REFERENCES production(id) ON DELETE CASCADE,
    INDEX idx_job_id (job_id),
    INDEX idx_stock_lot_id (stock_lot_id)
);

-- Alter inventory table: drop material_id, add variant_id
ALTER TABLE inventory
DROP FOREIGN KEY inventory_ibfk_1,
DROP COLUMN IF EXISTS units,
DROP COLUMN material_id;

ALTER TABLE inventory
ADD COLUMN variant_id BIGINT NOT NULL AFTER id,
ADD FOREIGN KEY (variant_id) REFERENCES material_variants(id) ON DELETE RESTRICT,
ADD INDEX idx_variant_id (variant_id);

-- Migration data: Create a default variant for each existing material
INSERT INTO material_variants (material_id, unit, reorderlevel, status)
SELECT id, 'ream', 0, 'ACTIVE'
FROM materials
WHERE id NOT IN (SELECT DISTINCT material_id FROM material_variants);
