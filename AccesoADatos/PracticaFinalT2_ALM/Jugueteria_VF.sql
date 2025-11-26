-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema practica_final_t2
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema practica_final_t2
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `practica_final_t2` DEFAULT CHARACTER SET utf8mb3 ;
USE `practica_final_t2` ;

-- -----------------------------------------------------
-- Table `practica_final_t2`.`juguete`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica_final_t2`.`juguete` (
  `idJuguete` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL DEFAULT NULL,
  `Descripcion` VARCHAR(45) NULL DEFAULT NULL,
  `Precio` FLOAT NULL DEFAULT NULL,
  `Stock` INT NULL DEFAULT NULL,
  `Categorida` ENUM('Pelotas', 'Muñecas', 'Vehiculos') NULL DEFAULT NULL,
  PRIMARY KEY (`idJuguete`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `practica_final_t2`.`zona`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica_final_t2`.`zona` (
  `idZona` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL DEFAULT NULL,
  `Descripcion` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `practica_final_t2`.`stand`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica_final_t2`.`stand` (
  `idStand` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL DEFAULT NULL,
  `Descripcion` VARCHAR(45) NULL DEFAULT NULL,
  `Zona_idZona` INT NOT NULL,
  PRIMARY KEY (`idStand`, `Zona_idZona`),
  INDEX `fk_Stand_Zona_idx` (`Zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_Stand_Zona`
    FOREIGN KEY (`Zona_idZona`)
    REFERENCES `practica_final_t2`.`zona` (`idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `practica_final_t2`.`stock`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica_final_t2`.`stock` (
  `CantidadDisponible` INT NOT NULL,
  `Stand_idStand` INT NOT NULL,
  `Stand_Zona_idZona` INT NOT NULL,
  `Juguete_idJuguete` INT NOT NULL,
  PRIMARY KEY (`Stand_idStand`, `Stand_Zona_idZona`, `Juguete_idJuguete`),
  INDEX `fk_Stock_Stand1_idx` (`Stand_idStand` ASC, `Stand_Zona_idZona` ASC) VISIBLE,
  INDEX `fk_Stock_Juguete1_idx` (`Juguete_idJuguete` ASC) VISIBLE,
  CONSTRAINT `fk_Stock_Juguete1`
    FOREIGN KEY (`Juguete_idJuguete`)
    REFERENCES `practica_final_t2`.`juguete` (`idJuguete`),
  CONSTRAINT `fk_Stock_Stand1`
    FOREIGN KEY (`Stand_idStand` , `Stand_Zona_idZona`)
    REFERENCES `practica_final_t2`.`stand` (`idStand` , `Zona_idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `practica_final_t2`.`cambio`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica_final_t2`.`cambio` (
  `idCambio` INT NOT NULL AUTO_INCREMENT,
  `Motivo` VARCHAR(45) NULL DEFAULT NULL,
  `Fecha` VARCHAR(45) NULL DEFAULT NULL,
  `Stock_Stand_idStand` INT NOT NULL,
  `Stock_Stand_Zona_idZona` INT NOT NULL,
  `Stock_Juguete_idJuguete` INT NOT NULL,
  `Stock_Stand_idStand1` INT NOT NULL,
  `Stock_Stand_Zona_idZona1` INT NOT NULL,
  `Stock_Juguete_idJuguete1` INT NOT NULL,
  PRIMARY KEY (`idCambio`),
  INDEX `fk_Cambio_Stock1_idx` (`Stock_Stand_idStand` ASC, `Stock_Stand_Zona_idZona` ASC, `Stock_Juguete_idJuguete` ASC) VISIBLE,
  INDEX `fk_Cambio_Stock2_idx` (`Stock_Stand_idStand1` ASC, `Stock_Stand_Zona_idZona1` ASC, `Stock_Juguete_idJuguete1` ASC) VISIBLE,
  CONSTRAINT `fk_Cambio_Stock1`
    FOREIGN KEY (`Stock_Stand_idStand` , `Stock_Stand_Zona_idZona` , `Stock_Juguete_idJuguete`)
    REFERENCES `practica_final_t2`.`stock` (`Stand_idStand` , `Stand_Zona_idZona` , `Juguete_idJuguete`),
  CONSTRAINT `fk_Cambio_Stock2`
    FOREIGN KEY (`Stock_Stand_idStand1` , `Stock_Stand_Zona_idZona1` , `Stock_Juguete_idJuguete1`)
    REFERENCES `practica_final_t2`.`stock` (`Stand_idStand` , `Stand_Zona_idZona` , `Juguete_idJuguete`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `practica_final_t2`.`empleado`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica_final_t2`.`empleado` (
  `idEmpleado` INT NOT NULL AUTO_INCREMENT,
  `Nombre` VARCHAR(45) NULL DEFAULT NULL,
  `Cargo` ENUM('Vendedor', 'Empleado') NULL DEFAULT NULL,
  `FechaIngreso` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`idEmpleado`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `practica_final_t2`.`venta`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `practica_final_t2`.`venta` (
  `idVenta` INT NOT NULL AUTO_INCREMENT,
  `Fecha` DATE NULL DEFAULT NULL,
  `PrecioTotal` FLOAT NULL DEFAULT NULL,
  `TipoPago` ENUM('Efectivo', 'Tarjeta') NULL DEFAULT NULL,
  `Juguete_idJuguete` INT NOT NULL,
  `Empleado_idEmpleado` INT NOT NULL,
  `Stand_idStand` INT NOT NULL,
  `Stand_Zona_idZona` INT NOT NULL,
  PRIMARY KEY (`idVenta`),
  INDEX `fk_Venta_Juguete1_idx` (`Juguete_idJuguete` ASC) VISIBLE,
  INDEX `fk_Venta_Empleado1_idx` (`Empleado_idEmpleado` ASC) VISIBLE,
  INDEX `fk_Venta_Stand1_idx` (`Stand_idStand` ASC, `Stand_Zona_idZona` ASC) VISIBLE,
  CONSTRAINT `fk_Venta_Empleado1`
    FOREIGN KEY (`Empleado_idEmpleado`)
    REFERENCES `practica_final_t2`.`empleado` (`idEmpleado`),
  CONSTRAINT `fk_Venta_Juguete1`
    FOREIGN KEY (`Juguete_idJuguete`)
    REFERENCES `practica_final_t2`.`juguete` (`idJuguete`),
  CONSTRAINT `fk_Venta_Stand1`
    FOREIGN KEY (`Stand_idStand` , `Stand_Zona_idZona`)
    REFERENCES `practica_final_t2`.`stand` (`idStand` , `Zona_idZona`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
