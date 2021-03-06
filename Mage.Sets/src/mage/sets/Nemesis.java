/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets;

import mage.cards.ExpansionSet;
import mage.constants.Rarity;
import mage.constants.SetType;

/**
 *
 * @author North
 */
public class Nemesis extends ExpansionSet {

    private static final Nemesis instance = new Nemesis();

    public static Nemesis getInstance() {
        return instance;
    }

    private Nemesis() {
        super("Nemesis", "NEM", ExpansionSet.buildDate(2000, 1, 5), SetType.EXPANSION);
        this.blockName = "Masques";
        this.parentSet = MercadianMasques.getInstance();
        this.hasBasicLands = false;
        this.hasBoosters = true;
        this.numBoosterLands = 0;
        this.numBoosterCommon = 11;
        this.numBoosterUncommon = 3;
        this.numBoosterRare = 1;
        this.ratioBoosterMythic = 0;
        cards.add(new SetCardInfo("Accumulated Knowledge", 26, Rarity.COMMON, mage.cards.a.AccumulatedKnowledge.class));
        cards.add(new SetCardInfo("Aether Barrier", 27, Rarity.RARE, mage.cards.a.AetherBarrier.class));
        cards.add(new SetCardInfo("Air Bladder", 28, Rarity.COMMON, mage.cards.a.AirBladder.class));
        cards.add(new SetCardInfo("Ancient Hydra", 76, Rarity.UNCOMMON, mage.cards.a.AncientHydra.class));
        cards.add(new SetCardInfo("Angelic Favor", 1, Rarity.UNCOMMON, mage.cards.a.AngelicFavor.class));
        cards.add(new SetCardInfo("Animate Land", 101, Rarity.UNCOMMON, mage.cards.a.AnimateLand.class));
        cards.add(new SetCardInfo("Arc Mage", 77, Rarity.UNCOMMON, mage.cards.a.ArcMage.class));
        cards.add(new SetCardInfo("Ascendant Evincar", 51, Rarity.RARE, mage.cards.a.AscendantEvincar.class));
        cards.add(new SetCardInfo("Avenger en-Dal", 2, Rarity.RARE, mage.cards.a.AvengerEnDal.class));
        cards.add(new SetCardInfo("Battlefield Percher", 52, Rarity.UNCOMMON, mage.cards.b.BattlefieldPercher.class));
        cards.add(new SetCardInfo("Belbe's Percher", 53, Rarity.COMMON, mage.cards.b.BelbesPercher.class));
        cards.add(new SetCardInfo("Belbe's Portal", 127, Rarity.RARE, mage.cards.b.BelbesPortal.class));
        cards.add(new SetCardInfo("Blastoderm", 102, Rarity.COMMON, mage.cards.b.Blastoderm.class));
        cards.add(new SetCardInfo("Blinding Angel", 3, Rarity.RARE, mage.cards.b.BlindingAngel.class));
        cards.add(new SetCardInfo("Bola Warrior", 78, Rarity.COMMON, mage.cards.b.BolaWarrior.class));
        cards.add(new SetCardInfo("Carrion Wall", 54, Rarity.UNCOMMON, mage.cards.c.CarrionWall.class));
        cards.add(new SetCardInfo("Chieftain en-Dal", 4, Rarity.UNCOMMON, mage.cards.c.ChieftainEnDal.class));
        cards.add(new SetCardInfo("Cloudskate", 29, Rarity.COMMON, mage.cards.c.Cloudskate.class));
        cards.add(new SetCardInfo("Coiling Woodworm", 103, Rarity.UNCOMMON, mage.cards.c.CoilingWoodworm.class));
        cards.add(new SetCardInfo("Complex Automaton", 128, Rarity.RARE, mage.cards.c.ComplexAutomaton.class));
        cards.add(new SetCardInfo("Dark Triumph", 55, Rarity.UNCOMMON, mage.cards.d.DarkTriumph.class));
        cards.add(new SetCardInfo("Daze", 30, Rarity.COMMON, mage.cards.d.Daze.class));
        cards.add(new SetCardInfo("Death Pit Offering", 56, Rarity.RARE, mage.cards.d.DeathPitOffering.class));
        cards.add(new SetCardInfo("Defender en-Vec", 5, Rarity.COMMON, mage.cards.d.DefenderEnVec.class));
        cards.add(new SetCardInfo("Defiant Falcon", 6, Rarity.COMMON, mage.cards.d.DefiantFalcon.class));
        cards.add(new SetCardInfo("Defiant Vanguard", 7, Rarity.UNCOMMON, mage.cards.d.DefiantVanguard.class));
        cards.add(new SetCardInfo("Divining Witch", 57, Rarity.RARE, mage.cards.d.DiviningWitch.class));
        cards.add(new SetCardInfo("Dominate", 31, Rarity.UNCOMMON, mage.cards.d.Dominate.class));
        cards.add(new SetCardInfo("Downhill Charge", 79, Rarity.COMMON, mage.cards.d.DownhillCharge.class));
        cards.add(new SetCardInfo("Ensnare", 32, Rarity.UNCOMMON, mage.cards.e.Ensnare.class));
        cards.add(new SetCardInfo("Fanatical Devotion", 8, Rarity.COMMON, mage.cards.f.FanaticalDevotion.class));
        cards.add(new SetCardInfo("Flame Rift", 80, Rarity.COMMON, mage.cards.f.FlameRift.class));
        cards.add(new SetCardInfo("Flowstone Armor", 131, Rarity.UNCOMMON, mage.cards.f.FlowstoneArmor.class));
        cards.add(new SetCardInfo("Flowstone Crusher", 81, Rarity.COMMON, mage.cards.f.FlowstoneCrusher.class));
        cards.add(new SetCardInfo("Flowstone Overseer", 82, Rarity.RARE, mage.cards.f.FlowstoneOverseer.class));
        cards.add(new SetCardInfo("Flowstone Slide", 83, Rarity.RARE, mage.cards.f.FlowstoneSlide.class));
        cards.add(new SetCardInfo("Flowstone Thopter", 132, Rarity.UNCOMMON, mage.cards.f.FlowstoneThopter.class));
        cards.add(new SetCardInfo("Flowstone Wall", 86, Rarity.COMMON, mage.cards.f.FlowstoneWall.class));
        cards.add(new SetCardInfo("Infiltrate", 33, Rarity.COMMON, mage.cards.i.Infiltrate.class));
        cards.add(new SetCardInfo("Jolting Merfolk", 34, Rarity.UNCOMMON, mage.cards.j.JoltingMerfolk.class));
        cards.add(new SetCardInfo("Kill Switch", 133, Rarity.RARE, mage.cards.k.KillSwitch.class));
        cards.add(new SetCardInfo("Kor Haven", 141, Rarity.RARE, mage.cards.k.KorHaven.class));
        cards.add(new SetCardInfo("Lawbringer", 10, Rarity.COMMON, mage.cards.l.Lawbringer.class));
        cards.add(new SetCardInfo("Lightbringer", 11, Rarity.COMMON, mage.cards.l.Lightbringer.class));
        cards.add(new SetCardInfo("Lin Sivvi, Defiant Hero", 12, Rarity.RARE, mage.cards.l.LinSivviDefiantHero.class));
        cards.add(new SetCardInfo("Massacre", 58, Rarity.UNCOMMON, mage.cards.m.Massacre.class));
        cards.add(new SetCardInfo("Mind Slash", 59, Rarity.UNCOMMON, mage.cards.m.MindSlash.class));
        cards.add(new SetCardInfo("Mogg Alarm", 93, Rarity.UNCOMMON, mage.cards.m.MoggAlarm.class));
        cards.add(new SetCardInfo("Moggcatcher", 96, Rarity.RARE, mage.cards.m.Moggcatcher.class));
        cards.add(new SetCardInfo("Mogg Salvage", 94, Rarity.UNCOMMON, mage.cards.m.MoggSalvage.class));
        cards.add(new SetCardInfo("Murderous Betrayal", 61, Rarity.RARE, mage.cards.m.MurderousBetrayal.class));
        cards.add(new SetCardInfo("Netter en-Dal", 13, Rarity.COMMON, mage.cards.n.NetterEnDal.class));
        cards.add(new SetCardInfo("Noble Stand", 14, Rarity.UNCOMMON, mage.cards.n.NobleStand.class));
        cards.add(new SetCardInfo("Off Balance", 15, Rarity.COMMON, mage.cards.o.OffBalance.class));
        cards.add(new SetCardInfo("Oracle's Attendants", 16, Rarity.RARE, mage.cards.o.OraclesAttendants.class));
        cards.add(new SetCardInfo("Oraxid", 35, Rarity.COMMON, mage.cards.o.Oraxid.class));
        cards.add(new SetCardInfo("Overlaid Terrain", 108, Rarity.RARE, mage.cards.o.OverlaidTerrain.class));
        cards.add(new SetCardInfo("Pack Hunt", 109, Rarity.RARE, mage.cards.p.PackHunt.class));
        cards.add(new SetCardInfo("Parallax Dementia", 62, Rarity.COMMON, mage.cards.p.ParallaxDementia.class));
        cards.add(new SetCardInfo("Parallax Inhibitor", 134, Rarity.RARE, mage.cards.p.ParallaxInhibitor.class));
        cards.add(new SetCardInfo("Parallax Nexus", 63, Rarity.RARE, mage.cards.p.ParallaxNexus.class));
        cards.add(new SetCardInfo("Parallax Tide", 37, Rarity.RARE, mage.cards.p.ParallaxTide.class));
        cards.add(new SetCardInfo("Parallax Wave", 17, Rarity.RARE, mage.cards.p.ParallaxWave.class));
        cards.add(new SetCardInfo("Phyrexian Driver", 64, Rarity.COMMON, mage.cards.p.PhyrexianDriver.class));
        cards.add(new SetCardInfo("Phyrexian Prowler", 65, Rarity.UNCOMMON, mage.cards.p.PhyrexianProwler.class));
        cards.add(new SetCardInfo("Plague Witch", 66, Rarity.COMMON, mage.cards.p.PlagueWitch.class));
        cards.add(new SetCardInfo("Predator, Flagship", 135, Rarity.RARE, mage.cards.p.PredatorFlagship.class));
        cards.add(new SetCardInfo("Rathi Assassin", 67, Rarity.RARE, mage.cards.r.RathiAssassin.class));
        cards.add(new SetCardInfo("Rathi Fiend", 68, Rarity.UNCOMMON, mage.cards.r.RathiFiend.class));
        cards.add(new SetCardInfo("Rathi Intimidator", 69, Rarity.COMMON, mage.cards.r.RathiIntimidator.class));
        cards.add(new SetCardInfo("Rath's Edge", 142, Rarity.RARE, mage.cards.r.RathsEdge.class));
        cards.add(new SetCardInfo("Refreshing Rain", 110, Rarity.UNCOMMON, mage.cards.r.RefreshingRain.class));
        cards.add(new SetCardInfo("Rejuvenation Chamber", 137, Rarity.UNCOMMON, mage.cards.r.RejuvenationChamber.class));
        cards.add(new SetCardInfo("Reverent Silence", 111, Rarity.COMMON, mage.cards.r.ReverentSilence.class));
        cards.add(new SetCardInfo("Rhox", 112, Rarity.RARE, mage.cards.r.Rhox.class));
        cards.add(new SetCardInfo("Rising Waters", 38, Rarity.RARE, mage.cards.r.RisingWaters.class));
        cards.add(new SetCardInfo("Rootwater Commando", 39, Rarity.COMMON, mage.cards.r.RootwaterCommando.class));
        cards.add(new SetCardInfo("Rootwater Thief", 40, Rarity.RARE, mage.cards.r.RootwaterThief.class));
        cards.add(new SetCardInfo("Rusting Golem", 138, Rarity.UNCOMMON, mage.cards.r.RustingGolem.class));
        cards.add(new SetCardInfo("Saproling Burst", 113, Rarity.RARE, mage.cards.s.SaprolingBurst.class));
        cards.add(new SetCardInfo("Saproling Cluster", 114, Rarity.RARE, mage.cards.s.SaprolingCluster.class));
        cards.add(new SetCardInfo("Seahunter", 41, Rarity.RARE, mage.cards.s.Seahunter.class));
        cards.add(new SetCardInfo("Seal of Cleansing", 18, Rarity.COMMON, mage.cards.s.SealOfCleansing.class));
        cards.add(new SetCardInfo("Seal of Doom", 70, Rarity.COMMON, mage.cards.s.SealOfDoom.class));
        cards.add(new SetCardInfo("Seal of Fire", 98, Rarity.COMMON, mage.cards.s.SealOfFire.class));
        cards.add(new SetCardInfo("Seal of Removal", 42, Rarity.COMMON, mage.cards.s.SealOfRemoval.class));
        cards.add(new SetCardInfo("Seal of Strength", 115, Rarity.COMMON, mage.cards.s.SealOfStrength.class));
        cards.add(new SetCardInfo("Shrieking Mogg", 99, Rarity.RARE, mage.cards.s.ShriekingMogg.class));
        cards.add(new SetCardInfo("Silkenfist Fighter", 19, Rarity.COMMON, mage.cards.s.SilkenfistFighter.class));
        cards.add(new SetCardInfo("Silkenfist Order", 20, Rarity.UNCOMMON, mage.cards.s.SilkenfistOrder.class));
        cards.add(new SetCardInfo("Sivvi's Ruse", 21, Rarity.UNCOMMON, mage.cards.s.SivvisRuse.class));
        cards.add(new SetCardInfo("Skyshroud Behemoth", 116, Rarity.RARE, mage.cards.s.SkyshroudBehemoth.class));
        cards.add(new SetCardInfo("Skyshroud Claim", 117, Rarity.COMMON, mage.cards.s.SkyshroudClaim.class));
        cards.add(new SetCardInfo("Skyshroud Cutter", 118, Rarity.COMMON, mage.cards.s.SkyshroudCutter.class));
        cards.add(new SetCardInfo("Skyshroud Poacher", 119, Rarity.RARE, mage.cards.s.SkyshroudPoacher.class));
        cards.add(new SetCardInfo("Skyshroud Ridgeback", 120, Rarity.COMMON, mage.cards.s.SkyshroudRidgeback.class));
        cards.add(new SetCardInfo("Sneaky Homunculus", 44, Rarity.COMMON, mage.cards.s.SneakyHomunculus.class));
        cards.add(new SetCardInfo("Spineless Thug", 71, Rarity.COMMON, mage.cards.s.SpinelessThug.class));
        cards.add(new SetCardInfo("Stampede Driver", 122, Rarity.UNCOMMON, mage.cards.s.StampedeDriver.class));
        cards.add(new SetCardInfo("Stronghold Discipline", 73, Rarity.COMMON, mage.cards.s.StrongholdDiscipline.class));
        cards.add(new SetCardInfo("Stronghold Gambit", 100, Rarity.RARE, mage.cards.s.StrongholdGambit.class));
        cards.add(new SetCardInfo("Stronghold Machinist", 46, Rarity.UNCOMMON, mage.cards.s.StrongholdMachinist.class));
        cards.add(new SetCardInfo("Stronghold Zeppelin", 47, Rarity.UNCOMMON, mage.cards.s.StrongholdZeppelin.class));
        cards.add(new SetCardInfo("Submerge", 48, Rarity.UNCOMMON, mage.cards.s.Submerge.class));
        cards.add(new SetCardInfo("Tangle Wire", 139, Rarity.RARE, mage.cards.t.TangleWire.class));
        cards.add(new SetCardInfo("Terrain Generator", 143, Rarity.UNCOMMON, mage.cards.t.TerrainGenerator.class));
        cards.add(new SetCardInfo("Treetop Bracers", 123, Rarity.COMMON, mage.cards.t.TreetopBracers.class));
        cards.add(new SetCardInfo("Trickster Mage", 49, Rarity.COMMON, mage.cards.t.TricksterMage.class));
        cards.add(new SetCardInfo("Vicious Hunger", 74, Rarity.COMMON, mage.cards.v.ViciousHunger.class));
        cards.add(new SetCardInfo("Viseling", 140, Rarity.UNCOMMON, mage.cards.v.Viseling.class));
        cards.add(new SetCardInfo("Voice of Truth", 25, Rarity.UNCOMMON, mage.cards.v.VoiceOfTruth.class));
        cards.add(new SetCardInfo("Volrath the Fallen", 75, Rarity.RARE, mage.cards.v.VolrathTheFallen.class));
        cards.add(new SetCardInfo("Wandering Eye", 50, Rarity.COMMON, mage.cards.w.WanderingEye.class));
        cards.add(new SetCardInfo("Woodripper", 125, Rarity.UNCOMMON, mage.cards.w.Woodripper.class));
    }
}
