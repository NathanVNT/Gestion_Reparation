-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Hôte : localhost
-- Généré le : lun. 26 août 2024 à 23:23
-- Version du serveur : 10.3.32-MariaDB
-- Version de PHP : 8.0.23

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `GestionReparation`
--
CREATE DATABASE IF NOT EXISTS `GestionReparation` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `GestionReparation`;

-- --------------------------------------------------------

--
-- Structure de la table `client`
--

CREATE TABLE `client` (
  `id` int(11) NOT NULL,
  `nom` varchar(200) NOT NULL,
  `prenom` varchar(200) NOT NULL,
  `telephone` varchar(20) NOT NULL,
  `mail` varchar(200) NOT NULL,
  `adresse` varchar(200) NOT NULL,
  `code_postal` varchar(20) NOT NULL,
  `ville` varchar(200) NOT NULL,
  `type` enum('Professionnel','Particulier') NOT NULL,
  `commentaire` text NOT NULL,
  `societe` varchar(500) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- --------------------------------------------------------

--
-- Structure de la table `reparateur`
--

CREATE TABLE `reparateur` (
  `id` int(11) NOT NULL,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `mot_de_passe` varchar(20) NOT NULL,
  `role` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `reparateur`
--

-- --------------------------------------------------------

--
-- Structure de la table `reparation`
--

CREATE TABLE `reparation` (
  `id` int(11) NOT NULL,
  `date` date NOT NULL,
  `details` text NOT NULL,
  `reparation_effectuee` text NOT NULL,
  `id_reparateur` int(11) NOT NULL,
  `tarif` decimal(10,0) DEFAULT NULL,
  `etat` enum('Non traité','En cours','Traité') NOT NULL,
  `numero_reparation` text NOT NULL,
  `id_client` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Index pour la table `client`
--
ALTER TABLE `client`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `reparateur`
--
ALTER TABLE `reparateur`
  ADD PRIMARY KEY (`id`);

--
-- Index pour la table `reparation`
--
ALTER TABLE `reparation`
  ADD PRIMARY KEY (`id`),
  ADD KEY `reparateur` (`id_reparateur`),
  ADD KEY `client` (`id_client`);

--
-- AUTO_INCREMENT pour les tables déchargées
--

--
-- AUTO_INCREMENT pour la table `client`
--
ALTER TABLE `client`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `reparateur`
--
ALTER TABLE `reparateur`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;

--
-- AUTO_INCREMENT pour la table `reparation`
--
ALTER TABLE `reparation`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Contraintes pour les tables déchargées
--

--
-- Contraintes pour la table `reparation`
--
ALTER TABLE `reparation`
  ADD CONSTRAINT `client` FOREIGN KEY (`id_client`) REFERENCES `client` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `reparateur` FOREIGN KEY (`id_reparateur`) REFERENCES `reparateur` (`id`) ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
