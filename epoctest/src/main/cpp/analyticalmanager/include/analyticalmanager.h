//
// Created by michde on 8/2/2017.
//

#ifndef EPOC_AM_NATIVE_BGE_ANALYTICALMANAGER_H
#define EPOC_AM_NATIVE_BGE_ANALYTICALMANAGER_H

#include <string>
#include <limits>
#include <cmath>
#include <cfloat>
#include <climits>

#include "globals.h"
#include "sensorreadings.h"
#include "NeuralNetCoeff.h"

#ifndef IN_OUT
#define IN_OUT
#endif

#ifndef IN
#define IN
#endif

#ifndef OUT
#define OUT
#endif

#ifndef M_E
#define M_E 2.71828182845904523536
#endif

#define isnan(x) std::isnan(x)
#define isinf(x) std::isinf(x)

using namespace Epoc::Common::Native::Definitions;

namespace Epoc {
    namespace AM {
        namespace Native {
            namespace Bge {

                class AnalyticalManager {
                private:
                    // 1.60 has new oxygen equation
                    // 1.61 has new humidity check
                    // 1.62 has no bicarb correction if bicarb is off and the fix for spike/dip detection
                    // 1.63.0 has the thermal/aging changes and i've gone to 3 digits for versions
                    // 1.63.1 final testconfig version
                    // 1.63.2 has pco2 correction for na+, k+, ca+, ph and hematocrit total protein correction
                    // 1.63.3 only applies pco2 correct if pco2 available
                    // 1.63.4 no longer need hematocrit to calculate pco2
                    // 1.63.5 spike and dip detection fix. na dependancy for hct fixed
                    // 1.63.6 has fix that calculates be even if na/hct aren't selected. also has thermal dip window
                    // 1.63.7 has the placing of the peak window parameters in the right place
                    // 1.63.8 change global.cs
                    // 1.63.9 changed oxygen equation.. 77 to 36.6 - etc + etc. added a = offset. b = slopefactor.
                    // 1.63.10 changed oxygen equation.. pcaltest is now a linear equation and does not include pcalrelease
                    // 1.63.11 fixed oxygen equation for age correction (d) and converting patm to mmhg
                    // 3.0.0 Changed testReading, SensorReadings.readings data type from ArrayList to List< >
                    // 3.0.1 skip realtime qc and humidity detection when the checkrealtime flag is not set
                    // 3.0.2 rolled iqc improvements in
                    // 3.0.3 new glucose and lactate equations
                    // 3.0.4 fixed a_high a_low problem
                    // 3.0.5 extended a_high, a_low glucose fix to lactate
                    // 3.0.6 added gold sensor and a gold sensor check for glucose
                    // 3.0.7 changed s_high and calconcentration high for glucose and lactate
                    // 3.0.8 added noise qc checks for glucose/lactate and pco2
                    // 3.0.9 fixed ! problem with gold and glucose cnc
                    // 3.0.10 used param15 as a bitmask and added postnoise - samplenoise into param13 for pco2, glu and lac
                    // 3.0.11 took out lactate for the field version of glucose
                    // 3.0.12 new noise based on curvature
                    // 3.0.13 took out new noise. took out samplenoise*calnoise and postnoise-samplenoise checks, and let co2 iqc failures test everything else so that mean-mean failures in qc mode will still let hematocrit through. with lactate
                    // 3.0.14 new drift for glucose based on mean. took out abs from mean differences. no lactate
                    // 3.0.15 new drift for glucose based on mean/sample. fixed return code bug in co2, glu and lact. no lactate
                    // 3.0.16 incorporated changes from code review may 19 2009
                    // 3.0.17 glucose and lactate can't give results under 0
                    // 3.0.18 New oxygen calculations
                    // 3.0.19 convert ambient pressure to mmhg since it comes in as kpa from external
                    // 3.0.20 corrected cmean for glucose
                    // 3.0.21 isample/ical limit for glucose
                    // 3.0.22 fixed messup with glucose new qc
                    // 3.0.23 backwards compatibility to old testconfigs with glucose new isample stuff
                    // 3.0.24 fixed the exception occurring in the gold sensor when fast or slow inject
                    // 3.1.0  lactate
                    // 3.1.1  new lactate
                    // 3.1.2  div/ instead of *div
                    // 3.1.3  glu and lact dependency on oxygen
                    // 3.1.4  new oxygen correction for glucose
                    // 3.1.5  chloride
                    // 3.2.0  new hematocrit correction for na & k. new realtime qc for hct.
                    // 3.2.1  code review changes and took out chloride
                    // 3.2.2  pco2 and ph heat transient windows and levels
                    // 3.2.3  added pco2 and po2 qa specific limits
                    // 3.2.4  cnc due to multiple failure or due to pco2 are flagged as requirements failed qc
                    // 3.2.5  if param9<0.0001 then l_na = 1 for hct. if param18 < 0.0001 don't do early injection check
                    // 3.2.6  added anca's ph algorithm
                    // 3.2.7  fixed a1calc and a2calc problem in ph
                    // 3.2.8  implemented ph check for uncorrected ph also
                    // 3.2.10 Implemented k2 fix for hematocrit
                    // 3.2.11 fixed problem with copysensorinfo where it wasnt copying param21 to param30
                    // 3.2.12 implemented pco2 and bicarb out of range requirement
                    // 3.2.13 made it so that over/under rr pco2 doesn't cnc potassium, sodium and calcium
                    // 3.2.14 pco2 correction for ph also
                    // 3.2.15 don't fail glucose for interferent if potassium is over 7
                    // 3.2.16 move the na+ and k+ checks in hematocrit inside the blood section of the code
                    // 3.3.0  na+, and k+ should override rc for hct only if success. late spike dip should only override early spike dip if success
                    // 3.3.1  pco2 no longer cnc's everything
                    // 3.3.2  fix for missing for loop
                    // 3.3.3  count 3 sensors for postorsample and latespikedip then sample delivery
                    // 3.3.4  counter being set to 0 inside the loop
                    // 3.3.5  new ph age correction
                    // 3.3.6  new noise
                    // 3.3.7  new oxygen bubble detector
                    // 3.3.8  changed bias slightly to fix huge noise calculations
                    // 3.3.9  po2 bubble fixed
                    // 3.3.10 pco2 change
                    // 3.3.11 ways to turn off pco2 and po2
                    // 3.3.12 bug in hct noise. and po2 cumulative noise now in translope and transnoise
                    // 3.3.13 introduce noise limit on po2 bubble window
                    // 3.3.14 late injection time offset fix on pco2
                    // 3.3.15 fix for pco2 late injection time offset
                    // 3.3.16 fix for adding late spike dip to co2 sample delivery and adding noise for 64 on param15
                    // 3.3.17 don't let insanity and reportable range success checking for hct override cnc
                    // 3.3.18 small qc fixes requested by daniel and mike
                    // 3.3.19 divprime glucose change for john k
                    // 3.3.20 pco2 param 19 is aqueous limit for drift low
                    // 3.3.21 only use param 19 for aq drift low on pco2 if it is nonzero
                    // 3.4.0  glucose and lactate identical and new
                    // 3.4.1  3 cal side failures cause all cnc. na+, k+ and ca++ early spike dip do not cause sample delivery
                    // 3.4.2  lactate early spike dip doesn't cause sample delivery
                    // 3.4.3  recoded lost lactate and glucose
                    // 3.4.4  glu and lac do o2 correction even if o2 fails qc. glu and lac before the lytes
                    // 3.4.5  fix for lactate readings for ca++ and na+
                    // 3.4.6  na+ lactate correction should be after bicarb correction
                    // 3.4.7  added some outputs for anca
                    // 3.4.8  pco2 cal drift, response and ph response for all analytes in output1,2,2 and swdiff for na k and ca in output4
                    // 3.4.9  pco2 slope 0/1/2/3
                    // 3.4.10 uncorrected ph in output5, uncorrected pco2 in output6 for all
                    // 3.4.11 lactate conc in output10
                    // 3.4.12 fixed psecond in glu/lac
                    // 3.4.13 co2 cal window failure cnc's glu and lac
                    // 3.4.14 took out early glucose spike dip cnc of o2
                    // 3.4.15 alternate slopefactor and offset for na+ ca++ when no lactate
                    // 3.4.16 agecorrection for ph also done for uncorrected
                    // 3.4.17 sodium fix
                    // 3.4.18 added mask for lactate/no lactate offset and slopefactor and reversed parameters
                    // 3.4.19 changed back the parameters 6 and 7 for lact and also only do the lact correction on new testconfig
                    // 3.4.20 do gold before glu/lac
                    // 3.4.21 wrong sensors causing sample delivery
                    // 3.5.0  fix uncorrected ph return code, hgb in qa mode even if hct out of range, ph early, no ph crash for AT, alternate noise for amp channels based on sib version, correct high cal limit for glu/lac, remove glu dependency on po2, early window only if hct > 10 or if hct fails
                    // 3.5.1  changes after code review
                    // 3.5.2  only use alternate noise limits for amps when the new params are > 0.0001
                    // 3.5.3  used 0.00001
                    // 3.5.4  correct params for noise for oxygen and ph dependency on hct
                    // 3.5.5  fixed break before calculateph that caused pco2readings to be null always
                    // 3.5.6  hgb calculated when return code is uncorrected hct (qa mode)
                    // 3.5.7  cal mean qc check
                    // 3.5.8  fixed age * ageoffset to age - ageoffset
                    // 3.5.9  rt qc doesn't start using mean until extra4
                    // 3.5.10 dont return mean errors until after extra4
                    // 3.5.11 bco and mco in hematocrit
                    // 3.5.12 3 new changes. redmine 4520
                    // 3.5.13 moved glu/lac after hct
                    // 3.5.14 shuffled some params around for glu/lac
                    // 3.5.15 glucose hct correction also applied to lac. hct params moved to 20, 21
                    // 3.5.16 perform early window on corrected hct if hct fails
                    // 3.5.17 bco and mco in hematocrit now done for both qa and blood mode
                    // 3.5.18 dont perform glu/lact hct correction if hct fails iqc
                    // 3.7.0  additional window, cal mean age correction for potentiometrics, sample delivery if hct 11 or 12. cl/crea 510(k) was 3.6.x
                    // 3.7.1  dont do additional window if param50 is 0
                    // 3.7.2  dont do tau if tau is 0 (divide by 0)
                    // 3.7.3  tau correction applied to limits and not to cal mean itself.
                    // 3.7.4  removed sampledetect from additionalex calculation
                    // 3.7.5  uncorredtedpco2 correction in uncorrectedph
                    // 3.7.6  moved cal age correction into calculateex
                    // 3.7.7  incorporated creatinine logic into latest AM
                    // 3.7.8  return code change for crea
                    // 3.7.9  removed < 0 for crea
                    // 3.7.10 made the < 0 for crea, lac, glu, only for "if (!allowNegativeValues)"
                    // 3.7.11 added the extra amp channels in as sensors
                    // 3.7.12 added realtime checking for early crea window and all absolute windows. new crea equation.
                    // 3.7.14 additional window checking starts from 0
                    // 3.7.15 rounding agap, fix for crea ageslope
                    // 3.7.16 moved the counting of the d2 failures here from cardreader.cs, multiplied sampleex by calex
                    // 3.7.17 calculate additional window ahead of sample detect check is findparams, but no return code
                    // 3.7.18 3 qc failures for all cnc does not include crea if crea rc is interferent detected
                    // 3.7.19 Removed "else" from the dropthrough for RT point qc
                    // 3.8.0  for es 3.13.0
                    // 3.8.1  oxygen change. ca++ change.
                    // 3.8.2  fix for o2. new mask for ph early window multiply by calmean
                    // 3.8.3  rolled in 3.7.18 and 3.7.19 changes
                    // 3.8.4  removed all gold code. calcium additional drift in output 9. moved lactate before electrolytes
                    // 3.8.5  try/catch the precalculates
                    // 3.8.6  hemodilution in precalculate hct
                    // 3.8.7  precalchct acts like hct for qc mode
                    // 3.8.8  slopefactor fix in uncorrected ph
                    // 3.8.9  output1 is slopefactor in unc ph
                    // 3.8.10 output1 was overriden later
                    // 3.8.11 uncorrected ph slopefactor added in another location
                    // 3.8.12 restored output1
                    // 3.8.13 used precalculated sodium and potassium for precalculated hct
                    // 3.8.14 output8 used for hct both final and partial
                    // 3.8.15 output8 usage reversed
                    // 3.8.16 output1 and output2 for hct = sodium and potassium
                    // 3.8.17 check for pco2 != null in sodium and potassium
                    // 3.8.18 put hct outputs back the way they were
                    // 3.8.19 egfrj cnc if age > 120 or < 22
                    // 3.8.20 fixed return code problem in findparams and changed ref bubble criteria to treat pco2 dependencies as 1
                    // 3.8.21 fixed return code specific vs general issue in checkrealtime
                    // 3.8.22 only do recheck for glucose qc if return code is success (dont erase old return codes)
                    // 3.8.23 precalculate hct return isblood true for both success and uncorrectedhct. chloride tau/age fix.
                    // 3.8.24 calcium correction only on original slope and offset
                    // 3.8.25 calculate hct, calculate ex, and findparams all have qcAs which determines what dips and spikes get done as
                    // 3.8.26 fix for hct to clear requirementsfailedqc. outputs of calcium
                    // 3.8.27 removed calcium outputs
                    // 3.8.28 Creatinine launch changes
                    // 3.8.29 ignore highest and lowest points in creatinine early window
                    // 3.8.30 outputs for crea
                    // 3.8.31 new outputs in output1, 2 and 9 for crea
                    // 3.8.32 corrected estimated hct in blood mode. added drift crea high/low changes
                    // 3.8.33 changes to support creatinine realtime qc
                    // 3.8.34 output9 in crea
                    // 3.8.35 fixes to crea, gl and validate qc (additional window)
                    // 3.8.37 new crea iqc failure
                    // 3.8.38 changed crea abs check and added return code to failediqc
                    // 3.9.0  cl- now shows result when pco2 is under/over rr. japanese egfr.
                    // 3.9.1  changed glu/lac to use div = div * (1-(a*(div-b)))
                    // 3.9.2  fix for redmine 5301. if na or k are est, skip the adjustments, but do insanity and reportable range checks.
                    // 3.9.3  addition of hct short sample delivery failure
                    // 3.9.4  Allow age up to 120 for egfr and egfr-a
                    // 3.9.5  took out glucose/lactate high end change
                    // 3.9.6  changes for new crea qc
                    // 3.9.7  updated with code review changes. cnc for egfrj nan height or gender unknown. fixes to updatecrealimits
                    // 3.9.8  checking ex instead of mean only for new crea qc
                    // 3.9.9  calculate ex before validate all qc
                    // 3.9.11 fixed cre ageslope vs ageslopeaq issue
                    // 3.10.1 newest glucose equation
                    // 3.10.2 with outputs
                    // 3.10.3 y3c in param13
                    // 3.10.4 fix for second div
                    // 3.10.5 again second div
                    // 3.10.6 without outputs
                    // 3.10.7 Added sample drift qc high to sample delivery criteria
                    // 3.10.8 added max 0 to all age and sample injection glucose y values
                    // 3.10.9 added max 0 to div in old glucose equation also
                    // 3.10.10 backward compatibility to old glucose equation by masking for max 0
                    // 3.10.11 new M coeff for old glucose
                    // 3.11.0  Added Cl- (agap, bicarb) iqc changes
                    // 3.11.1  crea relaunch changes
                    // 3.11.2  changes for bicarb insanity. doesnt need cl-.
                    // 3.11.3  changes for cal drift / cal ex for crea
                    // 3.11.4  rc has to be == success to override return code for crea
                    // 3.11.5  post drift limits have to be set wide for crea relaunch
                    // 3.12.0  fixed new (unused) glucose equation return code issue
                    // 3.12.1  added po2 iqc 13 sample delivery, glu/lac exponent changes and 2014 crea equation
                    // 3.12.2  fixed creatinine parenthesis issue
                    // 3.13.0  add qa ranges to insanity
                    // 3.13.1  cnc crea if hct or bicarb not available. Zero current detection for amps.
                    // 3.13.2  noise/mean for amps based on param15
                    // 3.13.3  new crea dip detection
                    // 3.13.4  Crea September 2014 equation and moved rtqc return code setting here from the Host.
                    // 3.13.5  fixed hct/crea return code problem
                    // 3.13.6  fixed cl- slight issue with oic2
                    // 3.13.7  crea with outputs
                    // 3.13.8  added sample window all point check to failed iqc
                    // 3.13.9  Set outputs correctly
                    // 3.13.10 amp divided by mean only applies to detecting window dips and not for anything else
                    // 3.13.11 changes based on code review.
                    // 3.13.12 window dip always does curvature corrected noise. remove highest and lowest point from window
                    // 3.13.13 point removal only for crea cal window dips
                    // 3.13.14 2 calls to windowdips were adding sample detect to size of window
                    // 3.13.15 added points to skip to firstpoint in windowdips
                    // 3.13.16 fixed noise in window dips
                    // 3.13.17 noise is calculated as the middle point in the window with points to either side
                    // 3.13.18 Added partials back in for crea
                    // 3.13.19 ensure that crea outputs aren't being overwritten with stock outputs
                    // 3.13.20 alphabicarb now zeroed out for creasep2014
                    // 3.13.21 pass in first and last point as temporary params in detectwindowdips
                    // 3.13.22 noise calculation in detectwindowdips was missing 1 time period.
                    // 3.13.23 highest and lowest point replaced with surrouding points mean. no curved noise in windowdips
                    // 3.13.24 don't move back crea cal window if there is a drift low failure
                    // 3.13.25 changes for second/mean. no cal dependent moveback if caldrift failure. calculate ex continues on failure and crea goes through calculateex again if div over limit
                    // 3.13.26 Fixed issue with second derivative points left and right and * 100 in windowdips and findwindowparams
                    // 3.13.27 fixed issue where all calwindowmoved back indicators were 6
                    // 3.13.28 split the cal window drift return code from the cal window dip
                    // 3.13.29 separate points in window for window dip for crea for cal and sample
                    // 3.13.30 check low limit only for second in window dips
                    // 3.13.31 Fixed issue where failedqcever was not causing dependent cnc. added failedqcever to calfailedqc
                    // 3.13.32 Changed calculateph to return the right return code
                    // 3.13.33 for debugging purposes, when calculatebgesensor catches an exception, it uses a special number to indicate this
                    // 3.13.34 reverted back to double.nan instead of the special number
                    // 3.13.35 moved the failedqc ever checks before reportable range
                    // 3.14.0  First version for ES 3.20.0. 2015 March mandatory release. Crea bubble detection, separate blood and aq coeffs,
                    // 3.14.1  Crea hct-est changes
                    // 3.14.2  Crea o2pivot changed to o2concthreshold
                    // 3.14.3  backward compatibility mask for crea sample bubble checking
                    // 3.14.4  backward compatibility mask for o2concthreshold
                    // 3.14.5  output10 for b,b2,b3 clause and switched o2pivot and threshold inc rea
                    // 3.14.6  more outputs for co2
                    // 3.14.7  outputs when sensor != pco2
                    // 3.14.8  don't send age corrections into calculate ex for sw peak window
                    // 3.14.9  outputs for pco2 and cl-
                    // 3.14.10 use fsensor in calculateex instead of f
                    // 3.14.11 pco2 slopefactor used old slopefactor from sensorinfo instead of blood/qa one
                    // 3.14.12 fixes for ph, uncorrectedph and sodium
                    // 3.14.13 set hct-est when in qa mode to 0
                    // 3.14.14 set hctestrequirementsfailedqc to false for qa mode in crea
                    // 3.14.15 took out crea partials
                    // 3.14.16 changes after code review
                    // 3.14.17 Was not passing age corrections in for uncorrected pco2
                    // 3.14.18 check po2 against hard coded device reportable ranges in calculateCreatinine
                    // 3.14.19 Fix isBlood determination with Hct over/under rr and req failed qc
                    // 3.14.20 A, A-a, a/A with comments from code review
                    // 3.15.0  First AM for ES 3.22.0. .200 with partials for cl-
                    // 3.15.1  Skip h,j,k correction if !estimate for Na and K and add K sensor in age corrections.
                    // 3.15.2  K correction paranthesis fix
                    // 3.15.3  Without outputs
                    // 3.15.4  add sample detect to fwindow start
                    // 3.15.5  only use h j k coefficients if lactate has not failed
                    // 3.15.6  when lactate correction not enabled, need to use h j k in na, k, ca
                    // 3.15.7  cl- bsensor for aqueous only when ageand30c flag on
                    // 3.15.8  cl- for aqueous to use legacy check and not skip the check
                    // 3.15.9  Changes as per es 3.22 code review
                    // 3.15.10 Restore the check for sodiumcalciumlactate correction in ca,k. add to na+
                    // 3.16.0  March 2016 mandatory release
                    // 3.16.1  Added mask for lactate high threshold
                    // 3.16.2  crea return code changed back
                    // 3.16.3  CalculateCreatinine with hard code SampleConcLowThreshold = (decimal)1; instead of getting from sensorInfo.tPlus; CalDriftFactor = (decimal)1.6 instead of getting from sensorInfo.tMinus;
                    // 3.16.4  for CalculateCreatinine, if new formula (from 3.16.3) is applied, don't use old one.
                    // 3.16.5  for CalculateCalcium, (1)use param54 as GSensor value in CalculateEX if ISBloodMatrix == false (2)final correction formula is applied to get calciumReadings.result
                    // 3.17.0  (1) in CalculateEx, include an equals clause when comparing RawMvSensor and MvCut (2) Implement Lactate Cal Drift correction term as per JK e-mail
                    //         (3) ths dependency of hct failQC, or hct recovery is requested ONLY for blood mode.
                    //         (4) add 10 outputs in Levels, Implement F-factor; (5) AW Drift correction for Hct; (6) Hct window rounding; (7) Add IsBloodMatrix=TRUE to criteria for AW drift correction for pCO2.
                    // 3.17.1  (1) add The mask value to enable isBlood criteria for AW drift correction for pCO2 (0x80)
                    // 3.17.2  (1) add The mask value to enable isBlood criteria for AW drift correction for Uncorrected pCO2 (0x80)
                    // 3.17.3  (1) add The mask value to enable !isBlood criteria for !AW drift correction for Uncorrected pCO2 (0x80)
                    // 3.18.0  For ES 3.25.0. Lactate changes. Redmine
                    // 3.18.1  Small fixes for Lactate equation
                    // 3.18.2  Changes made after official code review to calculateglucoseorlactate
                    // 3.18.3  update to use D1/D2 depending on divcut in calculateglucoseorlactate
                    // 3.18.103 Updated to include BUN & measured TCO2
                    // 3.19.0  Updates for ES 3.26.0. Updated Crea, X_Factor in Cl-.
                    // 3.19.1  Moved the Cl- X_Factor after lac correction
                    // 3.19.2  Implemented f factor change for ISEs
                    // 3.19.2  Implemented BUN smoothing algorithm
                    // 3.19.3  New volume corrections
                    // 3.19.4  Added mask for new crea age correction logic
                    // 3.19.5  Changes after code review
                    // 3.19.6  (1)originalAdditionalWindow should be from sensorInfo.param49 instead of sensorInfo.param50 in PreCalculateCalcium and should be double
                    //         (2)move volume-calculation after x-calculation in CalculateCalcium
                    //         (3)move volume-calculation after Lact-calculation in CalculateSodium
                    //         (4)move volume-calculation after Lact-calculation in CalculatePotassium
                    // 3.19.7  Find maximumSlope and find new window based on maximumSlope in PreCalculateCalcium
                    // 3.19.8  Replace by Math.Abs(((decimal)bubbleDetect - (decimal)sensorInfo.InjectionTimeOffset)) in CalculateEx when useAbsoluteValueInInjectionTime is applied for FPrime and FDoublePrime calculation
                    // 3.19.9  CalciumAdditionalDrift is used when C++ under or over reportable.
                    // 3.19.10 Lactate change Redmine 13983
                    // 3.19.11 Changes after second code review
                    // 3.19.12 Fix to keep calcium additional drift window location across several Host calculations
                    // 4.0.0   New Native C++ Analytical Manager (ported from the C# AM v3.19.12)
                    //         NOTE: Analytical Manager was translated from C# to C++ from svn ://cc:3691/Host3x/branches/3.26.0/src/AnalyticalManager
                    //                  - The original port of Analytical Manager was done for AM v3.19.8 (SVN tag 15894)
                    //                  - The new C++ Analytical Manager v3.19.8 was updated to v3.19.12 (SVN tag 16051)
                    // 4.0.1    Updated C++ AM:BGEM to match AM:BGEM 3.19.12.101, which adds adjustments for use with NextGen:
                    //              AnalyticalManager::IsChannelTypeASensor() now also returns true for ChannelType CH_2_A1 through ChannelType::CH_2_A4
                    // 4.0.2    Updated the window search loop for FindWindowParams() as per changes by Aditya Matam(co-op student), to reduce computation time, as well as small changes to compile the AM C++ code on reader hardware.
                    // 4.1.0    (1) Port C# AM changes for versions 3.19.13 to 3.21.5 from SVN revisions 16767 to 17848:
                    //              3.19.13 (1) introduce two coeffs to Crea calculation
                    //                      (2) Lactate early injection logic (for SRS requirement 4.1.1.5.5.8.13, redmine Feature Request #14815)
                    //                      (3) BUN corrections: injection time modification and K+ correction (for SRS requirement 4.1.1.5.5.8.3 and 4.1.1.5.5.8.17, redmine Feature Request #14816)
                    //                      (4) Crea final conc age correction; modified upper CalExCrea limit logic.(for SRS requirement 4.1.1.5.5.8.15 and 4.1.2.1.1.5.2, redmine Feature Request #14820)
                    //              3.19.14 (1) if (!FailedIQC(potassiumReading.returnCode) && !potassiumReading.requirementsFailedQC) skip K+ correction for BUN
                    //                      (2) use uncorrectedResponse instead of Response in BUN injection time correction.
                    //                      (3) remove two coeff for Crea calculation from AM
                    //              3.19.15 (1) if (!FailedIQC(potassiumReading.returnCode) && !potassiumReading.requirementsFailedQC) st KNH4KBUN = 0 correction for BUN
                    //              3.19.16 (1) CreaFinalAgeCorrect for Crea
                    //                      (2) check if potassium result is NaN for BUN aga correction.
                    //              3.19.17 (1) cnc Crea when pO2 out of device reportable range (only for blood mode).
                    //              3.19.18 Updated for pO2 failed iqc logic
                    //              3.19.19 Crea early age correction. Introduce three coefficients (EarlyAgeConc, EarlyAgeCut, EarlyAgeCorr) for SRS requirement 4.1.1.5.5.8.15.
                    //              3.19.20 (1) Split ComputeCalculatedeGFR() into separate ComputeCalculatedeGFRmdr() and ComputeCalculatedeGFRjpn() methods, and added ComputeCalculatedeGFRckd() and ComputeCalculatedeGFRswz() methods (which are always calculated)
                    //              3.20.0  (1) Remove the mV slope, AltkCO2 and Lactate logic for BUN (BUNmvSlopAltkCO2LacLogicMask = 0x40000) (SRS-RCIQC 3.32, requirements 4.1.1.5.5.8.3, 4.1.1.5.5.8.17)
                    //                      (2) TCO2 blood/aq slope/offset and new x0 coefficient (requirement 4.1.1.5.5.8.18.) TCO2AQSlopeOffsetX0CoeffMask = 0x20000;
                    //                      (3) pCO2 age correction on concentrations below threshold, if aqueous (SRS-RCIQC 3.32 requirement 4.1.1.5.5.8.5.)
                    //                      (4) Added Neural Network coefficiants and calcuations for Creatining (SRS-RCIQC 3.32 requirement 4.1.1.5.5.8.15.2)
                    //                      (5) output18 = invisibleBUN.calMean for Chloride (requested by Ho, Uyen)
                    //                      (6) output17 = pCO2 final concentration for all sensors (requested by Ho, Uyen)
                    //                      (7) Cl- correction when BUN is present (use gold sensor for invisible BUN) (SRS-RCIQC 3.32 updated, requirements 4.1.1.5.5.8.11, 4.1.1.5.5.8.14.)
                    //                      (8)implement binary search in FindWindowParam function
                    //                      (9)Do not report negative numbers for pCO2, TCO2, cHCO3+, cTCO2 (QA mode)
                    //              3.20.1  (1) Updated Crea Neural Net calculations to use 0 (zero) for Hct when "Hct < 0", or if the test mode is 'QA'.
                    //                      (2) Placed the Crea Neural Net calculations code in a "#if !PocketPC" conditional compile section so that it is not executed on the Host.
                    //              3.20.2  (1) Fixed bug: AM throw exception when pco2 is NaN and when try to cast pco2 reading from double to decimal for output17 in CalculateBGE
                    //              3.21.0  (1) Feature Request : Modified age corrections for BUN response (SRS-RCIQC requirement 4.1.1.5.5.8.3).
                    //                      (2) Feature Request : Added PCO2 Heater Correction (SRS-RCIQC requirement 4.1.1.5.5.8.5).
                    //              3.21.1  CalculatePCO2() and ApplyDriftTermCorrectedPCO2() updated to use "isBlood" boolean to determine if blood or aqueous params should be used for calculations.
                    //              3.21.2  CalculateUncorrectedPCO2() calls ApplyDriftTermCorrectedPCO2() (SRS-RCIQC requirement 4.1.1.5.5.8.5).
                    //              3.21.3  Updated CalculateEx() to calculate and apply Drift Term Corrected response for PCO2 (updated SRS-RCIQC requirement 4.1.1.5.5.8.5).
                    //              3.21.4  Updated CalculateEx() to replace an erroneous '+=' in the calculation of the PCO2 response value
                    //              3.21.5  Updated CalculateEx() to use RawMv(sensor) for all MvCut comparisons, as per SRS-RCIQC requirement 4.1.1.5.5.8.3 and SRS-RCIQC requirement 4.1.1.5.5.8.5
                    //          (2) Renamed conditional compilation directive "PocketPC" to "EPOCAL_TARGET_HOST"
                    //          (3) Using "NEXT_GEN" conditional compilation directive:
                    //                  - toggle between NextGen changes made in AM 4.0.1 to IsChannelTypeASensor() and using Host3's AM v3.21.5 of the method of the method
                    //                  - toggle between NextGen changes made in AM 4.0.2 to FindWindowParams() and using Host3's AM v3.21.5 of the method
                    //                  - disable NextGen definitions in for the following enums: Analytes, Units
                    // 4.1.1    (1) Updated to build with Android NDK 18
                    //          (2) Fix for CalculateChloride()
                    //          (3) Removed redundant conditional compile flag where the 'allowNegativeValues' can be used to check if calculated values are not allowed to be negative
                    // 4.1.2    (1) Handling calculations where the original C# AM code would have thrown for Decimal calculations which result in +/- Infinity values for Double calculations.
                    // 4.1.3    (1) Update setting SensorLevel.outputXYZ values to mimic casting double to a Decimal value
                    // 4.1.4    (1) Handling out of range exceptions to mimic C#
                    // 4.1.5    (1) Updated Reading to use float
                    //          (2) Updated Math::Round() method to use std::nearbyint(), with the rounding mode set to "FE_TONEAREST", which rounds to the nearest even integer; this mimics C# Math.Round()
                    //          (3) Updated Math::Round() to throw exceptions for non-finite and out of range inputs, as well as for overflowed results to mimic C# Math.Round()
                    static const int versionMajor = 4;
                    static const int versionMinor = 1;
                    static const int versionMicro = 5;

                public:
                    static void Version(int& major, int& minor, int& micro);

                public:
                    struct AgeCorrectionsQABlood
                    {
                        double BSensor;
                        double B2Sensor;
                        double B3Sensor;
                        double MvCut;
                        double AgeCut;
                        double FSensor;
                        double FPrimeSensor;
                        double FDoublePrimeSensor;
                        double HSensor;
                        double JSensor;
                        double KSensor;
                        double GSensor;
                        double earlyAgeCut;
                        double LowMvInjTimeCut;
                        double HighMvInjTimeCut;
                    };

                private:
                    static const double fullNernstVal;

                public:
                    static double FullNernst();

                private:
                    static const double po2DeviceReportableLow;
                    static const double po2DeviceReportableHigh;
                    static const int MaxCalMoveBack = 5;
                    static const int DefaultPointsToLeftForSecondDerivative = 2;
                    static const int DefaultPointsToRightForSecondDerivative = 2;
                    static const int lowNoiseSIBVersion = 14;

                public:
                    static int LowNoiseSIBVersion();

                private:
                    static const int numPointsInPO2BubbleDetect = 150;
                    static const int hctpHThreshold = 10;
                    static const int numPointsInPO2BubbleDetectThatAreZero = 20;
                    static const double numSecondsNoEarlySample;
                    static const double numSecondsStartEarlySampleChecking;
                    static const double DefaultOxygenValueForGlu;
                    static const double GluSampleMeanCalMeanHighLimitDifferentLimits;
                    static const double readerNoiseLowAbsoluteRealtime;
                    static const double readerNoiseLowBubbleDetect;
                    static const double readerNoiseLowSampleDetect;
                    static const double readerNoiseLowAbsoluteAfterTestEnds;
                    static const double readerNoiseLowDefault;
                    static const double readerNoiseLowDefault2;
                    static const double HctAqThreshold;
                    static const double GlucoseDriftDeltaDriftLow;
                    static const double CreaAgeLimit;
                    static const int DaysofWeek;

                private:
                    static const int CalWindowMoveBackMask = 0x1;

                private:
                    static const int PostMeanMinusSampleMeanMask = 0x8;
                    static const int NewOxygenEquationMask = 0x10;

                private:
                    static const int pHEarlyWindowMask = 0x20;
                    static const int pO2BubbleDetectMask = 0x20;
                    static const int NewNoiseMask = 0x40;
                    static const int NewGlucoseAndLactateMask = 0x80;
                    static const int NewSodiumCalciumLactateCorrectionMask = 0x100;
                    static const int MultiplypHEarlyWindowByCalMeanMask = 0x800;
                    static const int CreatinineMeanMeanMask = 0x20;
                    static const int NewCreaQCMask = 0x80;
                    static const int NewestGlucoseMask = 0x100;
                    static const int DecimalMax0OldGlucoseMask = 0x200;

                private:
                    static const int AmpNoiseDividedMeanMask = 0x800;
                    static const int CreaNoiseVsSecondDipDetectMask = 0x1000;
                    static const int CreaSeptember2014EquationMask = 0x2000;
                    static const int PotsAndGluLacAqBloodMask = 0x1000;

                private:
                    static const int CreaFlagCMask = 0x8000;
                    static const int AgeAnd30CLogicMask = 0x10000;
                    static const int CreaCalDriftMask = 0x20000;
                    static const int CreaAgeCorrectionMask = 0x40000;
                    static const int LacHighLactateThreshMask = 0x4000;

                    static const int NewLactatepO2CorrectionMask = 0x10000; // new lactate po2 correction
                    static const int LactateEarlyInjectionMask = 0x20000; // used by lactate early injection 

                    static const int LacpO2Mask = 0x8000; // lactate po2 mask

                    static const int HctVolumeCorrectionMask = 0x100;

                    //PCO2
                    static const int pCO2newSlopeMask = 0x20; //used by PCO2
                    static const int AWDriftCorrectedPCO2Mask = 0x80; //used by PCO2
                    static const int DriftTermCorrectedPCO2Mask = 0x20000; //used by PCO2

                    // BUN
                    static const int BUNInjectionTimeModifyMask = 0x20000; // used by BUN injection time modification 
                    static const int BUNmvSlopAltkCO2LacLogicMask = 0x40000; // used by BUN, Remove the mV slope, AltkCO2 and Lactate logic for BUN
                    static const int BUNAgeCorrectionsResponseMask = 0x80000; // used by BUN, Modified age corrections for BUN response (SRS-RCIQC version 3.33, requirement 4.1.1.5.5.8.3.)

                    //Crea
                    static const int CreaFinalAgeCorrectMask = 0x40000; // used by Crea final conc age correction.
                    static const int NewUpperCalExCreaLimitMask = 0x80000; // used by Crea modified upper CalExCrea limit logic.

                    //TCO2
                    static const int TCO2AQSlopeOffsetX0CoeffMask = 0x20000; // used by TCO2. TCO2 blood/aq slope/offset and new x0 coefficient

                private:
                    static const double defaultTemperature;
                    static const double fio2RangeLow;
                    static const double fio2RangeHigh;
                    static const double rQRangeLow;
                    static const double rQRangeHigh;
                    static const double pH20TermOne;
                    static const double pH20TermTwo;
                    static const double pH20TermThree;

                private:
                    static const double tco2PhCutoff;
                    static const double tco2Pco2Factor;

                private:
                    static const double NormalNaforTCO2;
                    static const double bunBicarbFactor;

                    //eGFR
                    static const double eGFRmdrSmpConcCreaMin;
                    static const double eGFRmdrPatientAgeMin;
                    static const double eGFRmdrPatientAgeMax;

                    static const double eGFRjpnSmpConcCreaMin;
                    static const double eGFRjpnPatientAgeMin;
                    static const double eGFRjpnPatientAgeMax;

                    static const double eGFRckdSmpConcCreaMin;
                    static const double eGFRckdPatientAgeMin;
                    static const double eGFRckdPatientAgeMax;

                    static const double eGFRswzSmpConcCreaMin;
                    static const double eGFRswzPatientAgeMin;
                    static const double eGFRswzPatientAgeMax;
                    static const double eGFRswzPatientHeightMin;
                    static const double eGFRswzPatientHeightMax;

                private:
                    static bool PotsAndGluLacAqBlood(double param);

                private:
                    static bool CreaSeptember2014Equation(double param);

                private:
                    static bool CreaNoiseVsSecondDipDetect(double param);

                private:
                    static bool AgeAnd30CLogic(double param);

                private:
                    static bool HctVolumeCorrection(double param);

                private:
                    static bool CreaFlagC(double param);

                private:
                    static bool AmpNoiseDividedMean(double param);

                private:
                    static bool CreatinineMeanMean(double param);

                private:
                    static bool NewCreaQC(double param);

                private:
                    static bool CreaAgeCorrection(double param);

                private:
                    static bool MultiplypHEarlyWindowByCalMean(double param);

                private:
                    static bool pO2BubbleDetect(double param);

                private:
                    static bool pCO2newSlope(double param);

                private:
                    static bool NewOxygenEquation(double param);

                private:
                    static bool CalWindowMoveBack(double param);

                private:
                    static bool PostMeanMinusSampleMean(double param);

                private:
                    static bool pHEarlyWindow(double param);

                private:
                    static bool NewNoise(double param);

                private:
                    static bool NewGlucoseAndLactate(double param);

                private:
                    static bool NewSodiumCalciumLactateCorrection(double param);

                private:
                    static bool NewestGlucose(double param);

                private:
                    static bool DecimalMax0OldGlucose(double param);

                private:
                    static bool CreaCalDrift(double param);

                private:
                    static bool LacHighLactateThresh(double param);

                private:
                    static bool AWDriftCorrectedPCO2(double param);

                private:
                    static bool LacpO2(double param);

                private:
                    static bool NewLactatepO2Correction(double param);

                private:
                    static bool LactateEarlyInjection(double param);

                private:
                    static bool BUNInjectionTimeModify(double param);

                private:
                    static bool BUNmvSlopAltkCO2LacLogic(double param);

                private:
                    static bool TCO2AQSlopeOffsetX0Coeff(double param);

                private:
                    static bool CreaFinalAgeCorrect(double param);

                private:
                    static bool NewUpperCalExCreaLimit(double param);

                private:
                    static bool DriftTermCorrectedPCO2(double bitmaskParam);

                private:
                    static bool BUNAgeCorrectionsResponse(double param);

                public:
                    static RealTimeHematocritQCReturnCode CheckForEarlyInjection(IN_OUT std::shared_ptr<SensorReadings> hematocritReadings,
                                                                                 IN std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                                 IN RealTimeHematocritQCReturnCode previousReturnCode,
                                                                                 IN double airAfterFluidThreshold,
                                                                                 IN float lastRecordedTime,
                                                                                 IN double firstFluid);

                private:
                    static bool OnePointFailure(RealTimeHematocritQCReturnCode returnCode);

                    static bool OneOrTwoPointFailure(RealTimeHematocritQCReturnCode returnCode);

                public:
                    static RealTimeQCReturnCode PerformRealTimeQC(IN_OUT std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                                  IN RealTimeQC qcStruct,
                                                                  IN float lastRecordedTime);

                private:
                    static bool CheckReferenceBubble(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime);

                private:
                    static bool CheckCreatinineRealtimeQC(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime, bool checkOnlyAtRightTime);

                private:
                    static bool CheckAdditionalWindows(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime);

                private:
                    static bool CheckRealtimeQC(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime);

                private:
                    static bool HaveAllHumidityChecksFailed(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct);

                private:
                    static void CheckHumidity(std::vector<std::shared_ptr<SensorReadings>> &testReadings, RealTimeQC qcStruct, float lastRecordedTime);

                private:
                    static bool DetectWindowDips(std::shared_ptr<std::vector<Reading>> readings,
                                                 double windowStart,               // window start
                                                 double windowSize,                // window size
                                                 int pointsToSkip,                 // start after this many points
                                                 int maxNumberPoints,              // max number of points
                                                 int pointsInWindow,               // points in window
                                                 double noiseLimit,                // noise high
                                                 double flagParameter,             // param15 - flags param
                                                 bool divideByMeanToGetNoise,
                                                 double subtractFromValues,
                                                 double lowerLimitForNoise,
                                                 bool removeHighestLowest);

                private:
                    static ResultsCalcReturnCode FindParams(std::shared_ptr<std::vector<Reading>> readings,
                                                            double bubbleDetect,
                                                            double sampleDetect,
                                                            std::shared_ptr<SensorInfo> sensorInfo,
                                                            std::shared_ptr<Levels> levels,
                                                            TestMode testMode,
                                                            bool useT,
                                                            bool getSamplePeakParams,
                                                            bool divideByMeanToGetNoise,
                                                            double subtractFromValues,
                                                            TestMode qcAs,
                                                            double meanLowerLimitForNoise,
                                                            bool dependentWindowMoveBack);

                private:
                    static ResultsCalcReturnCode DetectDipsAndSpikes(std::shared_ptr<std::vector<Reading>> readings,
                                                                     std::shared_ptr<SensorInfo> sensorInfo,
                                                                     double sampleDetect,
                                                                     TestMode testMode,
                                                                     double lastTime,
                                                                     bool divideByMeanToGetNoise);

                private:
                    static double GetMean(std::shared_ptr<std::vector<Reading>> readings,
                                          int startAt,
                                          int howMany);

                private:
                    static ResultsCalcReturnCode CheckAllPointsWithinLimits(std::shared_ptr<SensorReadings> reading,
                                                                            int firstPoint,
                                                                            int lastPoint,
                                                                            double low,
                                                                            double high);

                private:
                    static double GetMean(std::shared_ptr<std::vector<Reading>> readings,
                                          double startTime,
                                          double windowSize);

                private:
                    static ResultsCalcReturnCode FindWindowParams(std::shared_ptr<std::vector<Reading>> readings,
                                                                  double windowStart,
                                                                  double windowSize,
                                                                  double &mean,
                                                                  double &slope,
                                                                  double &noise,
                                                                  double &second,
                                                                  int &firstPoint,
                                                                  int &lastPoint,
                                                                  int startAt,
                                                                  bool divideByMeanToGetNoise,
                                                                  double subtractFromValues,
                                                                  bool useNewNoise,
                                                                  double meanLowerLimitForNoise,
                                                                  int pointsToLeft,
                                                                  int pointsToRight);
#ifndef NEXT_GEN
                private:
                    static int GetFirstLastPoint(std::shared_ptr<std::vector<Reading>> readings, int startAt, double windowStart, double windowSize, int &firstPoint, int &lastPoint);

                private:
                    static int GetFirstGreaterPoint(int startIdx, int endIdx, std::shared_ptr<std::vector<Reading>> readings, double windowStart);
#endif

                private:
                    static double GetSecondDerivative(std::shared_ptr<std::vector<Reading>> readings,
                                                      int firstPoint,
                                                      int lastPoint,
                                                      int pointsToLeftForSlope,
                                                      int pointsToRightForSlope,
                                                      double subtractFromValues);

                private:
                    static double GetSlope(std::shared_ptr<std::vector<Reading>> readings,
                                           int currentIndex,
                                           int includeToLeft,
                                           int includeToRight,
                                           double subtractFromValues);

                private:
                    static ResultsCalcReturnCode ValidateAllQC(std::shared_ptr<Levels> levels,
                                                               std::shared_ptr<SensorInfo> sensorInfo,
                                                               bool checkSWPeakLimits,
                                                               bool qcCalExInsteadOfCalMean);

                private:
                    static ResultsCalcReturnCode ValidateReportableRangeOnlyReturnCode(double &result, double low, double high);

                private:
                    static ResultsCalcReturnCode ValidateInsanityRangeOnlyReturnCode(double &result, double low, double high);

                private:
                    static double GetInterpolation(std::shared_ptr<std::vector<Reading>> readings,
                                                   double tMinus,
                                                   double tPlus,
                                                   double extrapTime,
                                                   double driftAtTMinus,
                                                   double driftAtTPlus);

                private:
                    static double GetInterpolation(std::shared_ptr<std::vector<Reading>> readings,
                                                   double tMinus,
                                                   double tPlus,
                                                   double extrapTime,
                                                   double driftAtTMinus,
                                                   double driftAtTPlus,
                                                   int startAt);

                private:
                    static ResultsCalcReturnCode CalculateEx(std::shared_ptr<std::vector<Reading>> readings,
                                                             std::shared_ptr<SensorInfo> sensorInfo,
                                                             double bubbleDetect,
                                                             double sampleDetect,
                                                             std::shared_ptr<Levels> levels,
                                                             bool toUseT,
                                                             TestMode testMode,
                                                             bool calculateDefaultResponse,
                                                             std::shared_ptr<SensorReadings> topHeaterReadings,
                                                             int ageOfCard,
                                                             double ambientTemperature,
                                                             bool useAbsoluteValueInInjectionTime,
                                                             bool getSamplePeakParams,
                                                             bool divideByMeanToGetNoise,
                                                             double subtractFromSampleWindow,
                                                             double ageParameter,
                                                             double tauParameter,
                                                             TestMode qcAs,
                                                             bool qcCalExInsteadOfCalMean,
                                                             double meanUpperLimitForNoise,
                                                             bool dependentMoveBackOccurred,
                                                             std::shared_ptr<AgeCorrectionsQABlood> ageCorrections,
                                                             bool isBlood);

                private:
                    static ResultsCalcReturnCode CalculateMeasuredTCO2(std::shared_ptr<SensorReadings> tco2Readings,
                                                                       std::shared_ptr<SensorReadings> pco2Reading,
                                                                       std::shared_ptr<SensorReadings> phReading,
                                                                       std::shared_ptr<SensorReadings> naReading,
                                                                       double bubbleDetect,
                                                                       double sampleDetect,
                                                                       double actualBicarb,
                                                                       TestMode testMode,
                                                                       std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                       int ageOfCard,
                                                                       double ambientTemperature,
                                                                       bool isBlood,
                                                                       bool applyVolumeCorrections);

                private:
                    static ResultsCalcReturnCode CalculateBUN(std::shared_ptr<SensorReadings> bunReadings,
                                                              std::shared_ptr<SensorReadings> pco2Reading,
                                                              std::shared_ptr<SensorReadings> hctReading,
                                                              std::shared_ptr<SensorReadings> lactateReading,
                                                              std::shared_ptr<SensorReadings> potassiumReading,
                                                              double bubbleDetect,
                                                              double sampleDetect,
                                                              double actualBicarb,
                                                              TestMode testMode,
                                                              std::shared_ptr<SensorReadings> topHeaterReadings,
                                                              int ageOfCard,
                                                              double ambientTemperature,
                                                              bool isBlood,
                                                              double calciumAdditionalDrift);

                private:
                    static ResultsCalcReturnCode CalculateSodium(std::shared_ptr<SensorReadings> sodiumReadings,
                                                                 std::shared_ptr<SensorReadings> pco2Reading,
                                                                 std::shared_ptr<SensorReadings> lactateReading,
                                                                 double bubbleDetect,
                                                                 double sampleDetect,
                                                                 double actualBicarb,
                                                                 TestMode testMode,
                                                                 std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                 int ageOfCard,
                                                                 double ambientTemperature,
                                                                 bool isBlood,
                                                                 bool applyVolumeCorrections,
                                                                 bool estimate,
                                                                 double calciumAdditionalDrift);

                private:
                    static ResultsCalcReturnCode CalMeanAgeCorrection(int ageOfCard, ResultsCalcReturnCode rc, std::shared_ptr<Levels> sensorLevels, std::shared_ptr<SensorInfo> sensorInfo, double ageParameter, double tauParameter);

                private:
                    static ResultsCalcReturnCode CalculatePotassium(std::shared_ptr<SensorReadings> potassiumReadings,
                                                                    std::shared_ptr<SensorReadings> pco2Reading,
                                                                    std::shared_ptr<SensorReadings> lactateReadings,
                                                                    double bubbleDetect,
                                                                    double sampleDetect,
                                                                    double actualBicarb,
                                                                    TestMode testMode,
                                                                    std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                    int ageOfCard,
                                                                    double ambientTemperature,
                                                                    bool isBlood,
                                                                    bool applyVolumeCorrections,
                                                                    bool estimate,
                                                                    double calciumAdditionalDrift);

                private:
                    static ResultsCalcReturnCode CalculateChloride(std::shared_ptr<SensorReadings> chlorideReadings,
                                                                   std::shared_ptr<SensorReadings> pco2Reading,
                                                                   std::shared_ptr<SensorReadings> lactateReadings,
                                                                   std::shared_ptr<SensorReadings> hctReadings,
                                                                   std::shared_ptr<SensorReadings> po2Reading,
                                                                   std::shared_ptr<SensorReadings> invBunReading,
                                                                   double bubbleDetect,
                                                                   double sampleDetect,
                                                                   double actualBicarb,
                                                                   TestMode testMode,
                                                                   std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                   int ageOfCard,
                                                                   double ambientTemperature,
                                                                   bool isBlood,
                                                                   bool applyVolumeCorrections,
                                                                   double calciumAdditionalDrift,
                                                                   bool bunAvailable);

                private:
                    static ResultsCalcReturnCode CalculatepH(std::shared_ptr<SensorReadings> pHReadings,
                                                             std::shared_ptr<SensorReadings> pco2Reading,
                                                             std::shared_ptr<SensorReadings> hctReading,
                                                             double bubbleDetect,
                                                             double sampleDetect,
                                                             double actualBicarb,
                                                             TestMode testMode,
                                                             std::shared_ptr<SensorReadings> topHeaterReadings,
                                                             int ageOfCard,
                                                             double ambientTemperature,
                                                             bool isBlood,
                                                             bool applyVolumeCorrections,
                                                             double calciumAdditionalDrift);

                private:
                    static bool PreCalculateHematocrit(std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                       double bubbleDetect,
                                                       double sampleDetect,
                                                       TestMode testMode,
                                                       bool applyHemodilution,
                                                       std::shared_ptr<SensorReadings> topHeaterReadings,
                                                       int ageOfCard,
                                                       double ambientTemperature);

                private:
                    static bool PreCalculateCalcium(std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                    double bubbleDetect,
                                                    double sampleDetect,
                                                    TestMode testMode,
                                                    std::shared_ptr<SensorReadings> topHeaterReadings,
                                                    int ageOfCard,
                                                    double ambientTemperature,
                                                    bool isBlood,
                                                    double bubbleWidth);

                private:
                    static ResultsCalcReturnCode CalculateUncorrectedpH(std::shared_ptr<SensorReadings> pHReadings,
                                                                        std::shared_ptr<SensorReadings> pco2Readings,
                                                                        double bubbleDetect,
                                                                        double sampleDetect,
                                                                        TestMode testMode,
                                                                        std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                        int ageOfCard,
                                                                        double ambientTemperature,
                                                                        bool isBlood,
                                                                        bool applyVolumeCorrections,
                                                                        double calciumAdditionalDrift);

                private:
                    static ResultsCalcReturnCode CalculateBGESensor(std::shared_ptr<SensorReadings> currentSensor,
                                                                    std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                                    double bicarb,
                                                                    double bubbleDetect,
                                                                    double sampleDetect,
                                                                    double ambientTemperature,
                                                                    double ambientPressure,
                                                                    TestMode testMode,
                                                                    std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                    int ageOfCard,
                                                                    bool applyHemodilution,
                                                                    int sibVersion,
                                                                    bool isBlood,
                                                                    bool applyVolumeCorrection,
                                                                    double calciumAdditionalDrift,
                                                                    bool allowNegativeValues);

                private:
                    static bool IsChannelTypeASensor(ChannelType channelType);

                public:
#ifdef _MANAGED
                    public enum class AnalyticalManagerParamType    // Managed C++/CLI
#else
                    enum class AnalyticalManagerParamType : int     // C++11
#endif
                    {
                        /// NOTICE: The order these enums are defined in shall remain unchanged,
                        /// only their values may be updated.
                        ///
                        /// The order each enum definition dictates the values applied to the enum's
                        /// Java representation, and thus any changes (adding, deleting, re-oerdering)
                        /// of enum elements must be reflected in the Java/JNI and C#/C++(CLI) code
                        /// as well.

                        bubbleDetect = 0,
                        sampleDetect = 1,
                        ambientTemperature = 2
                    };

                public:
                    class BGEParameters
                    {
                    private:
                        double parameter0;              // bubbleDetect
                        double parameter1;              // sampleDetect
                        float parameter2;               // ambientTemperature
                        float parameter3;               // ambientPressure
                        bool parameter4;                // applyMultiplicationFactor
                        FluidType parameter5;           // fluidType
                        std::shared_ptr<SensorReadings> parameter6;      // topHeaterPower
                        int parameter7;                 // ageOfCard
                        bool parameter8;                // applyHemodilution
                        double parameter9;              // bubbleWidth
                        int parameter10;                // sibVersionMajor
                        double parameter11;             // anionInsanityLow
                        double parameter12;             // anionInsanityHigh
                        double parameter13;             // bicarbInsanityLow
                        double parameter14;             // bicarbInsanityHigh
                        bool parameter15;               // applymTCO2
                        bool parameter16;               // sampleFailedQC

                        int count;

                        void SetCount(int indexOfAddedParam)
                        {
                            int newCount = indexOfAddedParam + 1;
                            if (count < newCount)
                                count = newCount;
                        }

                    public:
                        BGEParameters() : parameter0(-DBL_MAX),
                                          parameter1(-DBL_MAX),
                                          parameter2(-FLT_MAX),
                                          parameter3(-FLT_MAX),
                                          parameter4(false),
                                          parameter5(FluidType::Unknown),
                                          parameter6(nullptr),
                                          parameter7(INT_MIN),
                                          parameter8(false),
                                          parameter9(-DBL_MAX),
                                          parameter10(INT_MIN),
                                          parameter11(-DBL_MAX),
                                          parameter12(-DBL_MAX),
                                          parameter13(-DBL_MAX),
                                          parameter14(-DBL_MAX),
                                          parameter15(false),
                                          parameter16(false),
                                          count(0) {}

                        BGEParameters(double bubbleDetect,
                                      double sampleDetect,
                                      float ambientTemperature,
                                      float ambientPressure,
                                      bool applyMultiplicationFactor,
                                      FluidType fluidType,
                                      std::shared_ptr<SensorReadings> topHeaterPower,
                                      int ageOfCard,
                                      bool applyHemodilution,
                                      double bubbleWidth,
                                      int sibVersionMajor,
                                      double anionInsanityLow,
                                      double anionInsanityHigh,
                                      double bicarbInsanityLow,
                                      double bicarbInsanityHigh,
                                      bool applymTCO2,
                                      bool sampleFailedQC) : parameter0(bubbleDetect),
                                                             parameter1(sampleDetect),
                                                             parameter2(ambientTemperature),
                                                             parameter3(ambientPressure),
                                                             parameter4(applyMultiplicationFactor),
                                                             parameter5(fluidType),
                                                             parameter6(topHeaterPower),
                                                             parameter7(ageOfCard),
                                                             parameter8(applyHemodilution),
                                                             parameter9(bubbleWidth),
                                                             parameter10(sibVersionMajor),
                                                             parameter11(anionInsanityLow),
                                                             parameter12(anionInsanityHigh),
                                                             parameter13(bicarbInsanityLow),
                                                             parameter14(bicarbInsanityHigh),
                                                             parameter15(applymTCO2),
                                                             parameter16(sampleFailedQC),
                                                             count(17) {}

                        int Count() const { return count; }

                        bool AddParam(double value) { return SetParam(count, value); }

                        bool SetParam(int index, double value)
                        {
                            bool added = true;
                            switch (index)
                            {
                                case 0:
                                    parameter0 = value;
                                    break;

                                case 1:
                                    parameter1 = value;
                                    break;

                                case 9:
                                    parameter9 = value;
                                    break;

                                case 11:
                                    parameter11 = value;
                                    break;

                                case 12:
                                    parameter12 = value;
                                    break;

                                case 13:
                                    parameter13 = value;
                                    break;

                                case 14:
                                    parameter14 = value;
                                    break;

                                default:
                                    added = false;
                                    break;
                            }

                            if (added)
                                SetCount(index);

                            return added;
                        }

                        bool GetParam(int index, double *value) const
                        {
                            bool found = true;

                            if (index >= count)
                            {
                                // index out of range
                                found = false;
                            }
                            else
                            {
                                switch (index)
                                {
                                    case 0:
                                        *value = parameter0;
                                        break;

                                    case 1:
                                        *value = parameter1;
                                        break;

                                    case 9:
                                        *value = parameter9;
                                        break;

                                    case 11:
                                        *value = parameter11;
                                        break;

                                    case 12:
                                        *value = parameter12;
                                        break;

                                    case 13:
                                        *value = parameter13;
                                        break;

                                    case 14:
                                        *value = parameter14;
                                        break;

                                    default:
                                        found = false;
                                        break;
                                }
                            }

                            return found;
                        }

                        bool AddParam(float value) { return SetParam(count, value); }

                        bool SetParam(int index, float value)
                        {
                            bool added = true;
                            switch (index)
                            {
                            case 2:
                                parameter2 = value;
                                break;

                            case 3:
                                parameter3 = value;
                                break;

                            default:
                                added = false;
                                break;
                            }

                            if (added)
                                SetCount(index);

                            return added;
                        }

                        bool GetParam(int index, float *value) const
                        {
                            bool found = true;

                            if (index >= count)
                            {
                                // index out of range
                                found = false;
                            }
                            else 
                            {
                                switch (index)
                                {
                                case 2:
                                    *value = parameter2;
                                    break;

                                case 3:
                                    *value = parameter3;
                                    break;

                                default:
                                    found = false;
                                    break;
                                }
                            }

                            return found;
                        }

                        bool AddParam(bool value)
                        {
                            return SetParam(count, value);
                        }

                        bool SetParam(int index, bool value)
                        {
                            bool added = true;
                            switch (index)
                            {
                                case 4:
                                    parameter4 = value;
                                    break;

                                case 8:
                                    parameter8 = value;
                                    break;

                                case 15:
                                    parameter15 = value;
                                    break;

                                case 16:
                                    parameter16 = value;
                                    break;

                                default:
                                    added = false;
                                    break;
                            }

                            if (added)
                                SetCount(index);

                            return added;
                        }

                        bool GetParam(int index, bool *value) const
                        {
                            bool found = true;

                            if (index >= count)
                            {
                                // index out of range
                                found = false;
                            }
                            else 
                            {
                                switch (index)
                                {
                                    case 4:
                                        *value = parameter4;
                                        break;

                                    case 8:
                                        *value = parameter8;
                                        break;

                                    case 15:
                                        *value = parameter15;
                                        break;

                                    case 16:
                                        *value = parameter16;
                                        break;

                                    default:
                                        found = false;
                                        break;
                                }
                            }

                            return found;
                        }

                        bool AddParam(FluidType value)
                        {
                            return SetParam(count, value);
                        }

                        bool SetParam(int index, FluidType value)
                        {
                            bool added = true;
                            switch (index)
                            {
                                case 5:
                                    parameter5 = value;
                                    break;

                                default:
                                    added = false;
                                    break;
                            }

                            if (added)
                                SetCount(index);

                            return added;
                        }

                        bool GetParam(int index, FluidType *value) const
                        {
                            bool found = true;

                            if (index >= count)
                            {
                                // index out of range
                                found = false;
                            }
                            else 
                            {
                                switch (index)
                                {
                                    case 5:
                                        *value = parameter5;
                                        break;

                                    default:
                                        found = false;
                                        break;
                                }
                            }

                            return found;
                        }

                        bool AddParam(std::shared_ptr<SensorReadings> value)
                        {
                            return SetParam(count, value);
                        }

                        bool SetParam(int index, std::shared_ptr<SensorReadings> value)
                        {
                            bool added = true;
                            switch (index)
                            {
                                case 6:
                                    parameter6 = value;
                                    break;

                                default:
                                    added = false;
                                    break;
                            }

                            if (added)
                                SetCount(index);

                            return added;
                        }

                        bool GetParam(int index, std::shared_ptr<SensorReadings> *value) const
                        {
                            bool found = true;

                            if (index >= count)
                            {
                                // index out of range
                                found = false;
                            }
                            else
                            {
                                switch (index)
                                {
                                    case 6:
                                        *value = parameter6;
                                        break;

                                    default:
                                        found = false;
                                        break;
                                }
                            }

                            return found;
                        }

                        bool AddParam(int value)
                        {
                            return SetParam(count, value);
                        }

                        bool SetParam(int index, int value)
                        {
                            bool added = true;
                            switch (index)
                            {
                                case 7:
                                    parameter7 = value;
                                    break;

                                case 10:
                                    parameter10 = value;
                                    break;

                                default:
                                    added = false;
                                    break;
                            }

                            if (added)
                                SetCount(index);

                            return added;
                        }

                        bool GetParam(int index, int *value) const
                        {
                            bool found = true;

                            if (index >= count)
                            {
                                // index out of range
                                found = false;
                            }
                            else
                            {
                                switch (index)
                                {
                                    case 7:
                                        *value = parameter7;
                                        break;

                                    case 10:
                                        *value = parameter10;
                                        break;

                                    default:
                                        found = false;
                                        break;
                                }
                            }

                            return found;
                        }
                    };

                private:
                    static void SetAllReturnCodesCnc(std::vector<std::shared_ptr<SensorReadings>> &testReadings);

                public:
                    static void CalculateBGE(IN_OUT std::vector<std::shared_ptr<SensorReadings>> &testReadings, IN_OUT BGEParameters &parameters, IN bool allowNegativeValues);

                private:
                    static void SmoothBUNReadings(std::shared_ptr<SensorReadings> sensorReadings, std::shared_ptr<std::vector<Reading>>);

                private:
                    static void RestoreBUNReadings(std::shared_ptr<SensorReadings> sensorReadings, std::shared_ptr<std::vector<Reading>> originalBUNReadings);

                private:
                    static bool DetermineCalculatedResultsSampleDelivery(std::vector<std::shared_ptr<SensorReadings>> &testReadings, double anionInsanityLow, double anionInsanityHigh, double bicarbInsanityLow, double bicarbInsanityHigh, bool applymTCO2);

                private:
                    static bool EarlySpikeDipIsSampleDelivery(Sensors sensor);

                private:
                    static void SetAllNotFailedQCToCNC(std::vector<std::shared_ptr<SensorReadings>> &testReadings);

                private:
                    static bool EarlySpikeDip(ResultsCalcReturnCode rc);

                private:
                    static bool LateSpikeDip(ResultsCalcReturnCode rc);

                private:
                    static bool SpikeDipFailed(ResultsCalcReturnCode rc);

                private:
                    static bool CalFailedIQC(ResultsCalcReturnCode rc);

                private:
                    static bool PostOrSampleFailedIQC(ResultsCalcReturnCode rc);

                private:
                    static bool OutOfReportable(ResultsCalcReturnCode rc);

                private:
                    static bool CanBeUsedAsInput(ResultsCalcReturnCode rc);

                private:
                    static bool CanBeMeasuredAsInput(std::shared_ptr<SensorReadings> reading);

                public:
                    static bool FailedIQC(ResultsCalcReturnCode rc);

                private:
                    static bool DetermineHctShortSampleDelivery(std::vector<std::shared_ptr<SensorReadings>> &testReadings,
                                                                double bubbleWidth,
                                                                double bubbleDetect,
                                                                double sampleDetect);

                private:
                    static ResultsCalcReturnCode CalculatePCO2(std::shared_ptr<SensorReadings> carbonDioxideReadings,
                                                               double bubbleDetect,
                                                               double sampleDetect,
                                                               double actualBicarb,
                                                               TestMode testMode,
                                                               std::shared_ptr<SensorReadings> topHeaterReadings,
                                                               int ageOfCard,
                                                               double ambientTemperature,
                                                               bool applyVolumeCorrections,
                                                               bool isBlood,
                                                               double calciumAdditionalDrift);

                private:
                    static double CalculateDriftTermCorrectedPCO2(std::shared_ptr<SensorInfo> sensorInfo, std::shared_ptr<Levels> levels, std::shared_ptr<SensorReadings> topHeaterReadings, bool isBlood);

                private:
                    static ResultsCalcReturnCode CalculateUncorrectedPCO2(std::shared_ptr<SensorReadings> carbonDioxideReadings,
                                                                          double bubbleDetect,
                                                                          double sampleDetect,
                                                                          TestMode testMode,
                                                                          std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                          int ageOfCard,
                                                                          double ambientTemperature,
                                                                          bool applyVolumeCorrections,
                                                                          bool isBlood,
                                                                          double calciumAdditionalDrift);

                private:
                    static ResultsCalcReturnCode CalculateCalcium(std::shared_ptr<SensorReadings> calciumReadings,
                                                                  std::shared_ptr<SensorReadings> pco2Reading,
                                                                  std::shared_ptr<SensorReadings> lactateReading,
                                                                  std::shared_ptr<SensorReadings> po2Reading,
                                                                  double bubbleDetect,
                                                                  double sampleDetect,
                                                                  double actualBicarb,
                                                                  TestMode testMode,
                                                                  std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                  int ageOfCard,
                                                                  double ambientTemperature,
                                                                  bool isBlood,
                                                                  bool applyVolumeCorrections,
                                                                  double calciumAdditionalDrift);

                private:
                    static ResultsCalcReturnCode CalculatePO2(std::shared_ptr<SensorReadings> oxygenReadings,
                                                              std::shared_ptr<SensorReadings> glucoseReadings,
                                                              double bubbleDetect,
                                                              double sampleDetect,
                                                              double ambientTemperature,
                                                              double pressure,
                                                              TestMode testMode,
                                                              int ageOfCard,
                                                              int sibVersion);

                private:
                    static bool pO2HasSampleBubble(std::shared_ptr<SensorReadings> oxygenReadings,
                                                   std::shared_ptr<SensorInfo> sensorInfo,
                                                   double sampleDetect,
                                                   double& refSlope,
                                                   double& refNoise,
                                                   double& refSecond);

                private:
                    static double DecimalMax0(double value);

                private:
                    static ResultsCalcReturnCode CalculateGlucose(std::shared_ptr<SensorReadings> glucoseReadings,
                                                                  double bubbleDetect,
                                                                  double sampleDetect,
                                                                  TestMode testMode,
                                                                  int ageOfCard,
                                                                  std::shared_ptr<SensorReadings> oxygenReadings,
                                                                  std::shared_ptr<SensorReadings> co2Readings,
                                                                  std::shared_ptr<SensorReadings> hctReadings,
                                                                  int sibVersion,
                                                                  bool isBlood,
                                                                  bool allowNegativeValues);

                private:
                    static ResultsCalcReturnCode CalculateLactate(std::shared_ptr<SensorReadings> lactateReadings,
                                                                  double bubbleDetect,
                                                                  double sampleDetect,
                                                                  TestMode testMode,
                                                                  int ageOfCard,
                                                                  std::shared_ptr<SensorReadings> oxygenReadings,
                                                                  std::shared_ptr<SensorReadings> co2Readings,
                                                                  std::shared_ptr<SensorReadings> hctReadings,
                                                                  int sibVersion,
                                                                  bool isBlood,
                                                                  bool allowNegativeValues);

                private:
                    static ResultsCalcReturnCode CalculateGlucoseOrLactate(std::shared_ptr<SensorReadings> passedReadings,
                                                                           double bubbleDetect,
                                                                           double sampleDetect,
                                                                           TestMode testMode,
                                                                           int ageOfCard,
                                                                           std::shared_ptr<SensorReadings> oxygenReadings,
                                                                           std::shared_ptr<SensorReadings> co2Readings,
                                                                           std::shared_ptr<SensorReadings> hctReadings,
                                                                           int sibVersion,
                                                                           bool isBlood,
                                                                           bool allowNegativeValues);

                private:
                    static ResultsCalcReturnCode CalculateCreatinine(std::shared_ptr<SensorReadings> creatinineReadings,
                                                                     double bubbleDetect,
                                                                     double sampleDetect,
                                                                     TestMode testMode,
                                                                     std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                     int ageOfCard,
                                                                     std::shared_ptr<SensorReadings> oxygenReadings,
                                                                     std::shared_ptr<SensorReadings> potassiumReadings,
                                                                     std::shared_ptr<SensorReadings> co2Readings,
                                                                     std::shared_ptr<SensorReadings> hctReadings,
                                                                     double actualBicarb,
                                                                     int sibVersion,
                                                                     bool isBlood,
                                                                     double ambientPressure,
                                                                     bool allowNegativeValues);

                private:
                    static std::shared_ptr<NeuralNetCoeff> GetNeuralNetCreaCoeff(std::string neuralNetRawCoeff, std::shared_ptr<Levels> sensorLevels,
                                                                                 double sampleDetect,
                                                                                 double bubbleDetect,
                                                                                 std::shared_ptr<SensorReadings> topHeaterReadings,
                                                                                 double ageOfCard,
                                                                                 double resultHct,
                                                                                 double resultOxygen,
                                                                                 double actualBicarb);

                private:
                    static void UpdateCreaLimits(std::shared_ptr<SensorInfo> sensorInfo, double ageOfCard, double sampleDetect);

                private:
                    // Unit conversion constants and functions
                    static double ConvertKpaTommHg(double kpa);

                private:
                    static const double mmHgtoKpa;
                    static const double mmolTomgdl;
                    static const double mmolTomeqlCa;
                    static const double meqlTomgdlCa;

                private:
                    static ResultsCalcReturnCode PreTestHematocrit(std::shared_ptr<SensorReadings> hematocritReadings,
                                                                   double fluidAfterFluidThreshold,
                                                                   double maxBubbleWidth,
                                                                   double bubbleDetect,
                                                                   TestMode testMode,
                                                                   double& sampleDetect);

                private:
                    static ResultsCalcReturnCode CalculateHematocrit(std::shared_ptr<SensorReadings> hematocritReadings,
                                                                     std::shared_ptr<SensorReadings> sodiumReadings,
                                                                     std::shared_ptr<SensorReadings> potassiumReadings,
                                                                     double bubbleDetect,
                                                                     double sampleDetect,
                                                                     TestMode testMode,
                                                                     bool applyHemodilution,
                                                                     TestMode qcAs,
                                                                     bool isEstimated,
                                                                     bool applyVolumeCorrections,
                                                                     bool isBlood,
                                                                     double calciumAdditionalDrift);

                private:
                    static std::shared_ptr<FinalResult> FindReading(std::vector<FinalResult> &results, Analytes analyteToFind);

                private:
                    static void ComputeCalculatedActualBicarbonate(std::vector<FinalResult> &measuredResults, std::vector<FinalResult> &calculatedResults);

                private:
                    static double ComputeCalculatedUncorrectedActualBicarbonate(double uncorrectedCO2,
                                                                                ResultsCalcReturnCode CO2ReturnCode,
                                                                                double uncorrectedpH,
                                                                                ResultsCalcReturnCode pHReturnCode);

                private:
                    static void ComputeCalculatedBaseExcessECF(std::vector<FinalResult> &measuredResults,
                                                               std::vector<FinalResult> &calculatedResults);

                private:
                    static void ComputeCalculatedBaseExcessBlood(std::vector<FinalResult> &measuredResults,
                                                                 std::vector<FinalResult> &calculatedResults);

                private:
                    static void ComputeCalculatedOxygenSaturation(std::vector<FinalResult> &measuredResults,
                                                                  std::vector<FinalResult> &calculatedResults);

                private:
                    static void ComputeCalculatedTotalCO2(std::vector<FinalResult> &measuredResults,
                                                          std::vector<FinalResult> &calculatedResults);

                private:
                    static void ComputeCalculatedAlveolarO2(std::vector<FinalResult> &measuredResults,
                                                            std::vector<FinalResult> &calculatedResults,
                                                            double ambientPressure,
                                                            double fio2,
                                                            double RQ);

                private:
                    static void ComputeCorrectedAlveolarO2(std::vector<FinalResult> &correctedResults,
                                                           double ambientPressure,
                                                           double fio2,
                                                           double RQ,
                                                           double patientTemperature);

                private:
                    static void ComputeCalculatedArtAlvOxDiff(std::vector<FinalResult> &measuredResults,
                                                              std::vector<FinalResult> &calculatedResults);

                private:
                    static void ComputeCorrectedArtAlvOxDiff(std::vector<FinalResult> &correctedResults);

                private:
                    static void ComputeCalculatedArtAlvOxRatio(std::vector<FinalResult> &measuredResults,
                                                               std::vector<FinalResult> &calculatedResults);

                private:
                    static void ComputeCorrectedArtAlvOxRatio(std::vector<FinalResult> &correctedResults);

                private:
                    static void ComputeCalculatedBUNCreaRatio(std::vector<FinalResult> &measuredResults,
                                                              std::vector<FinalResult> &calculatedResults);

                private:
                    static void ComputeCalculatedHemoglobin(std::vector<FinalResult> &measuredResults,
                                                            std::vector<FinalResult> &calculatedResults,
                                                            TestMode testMode);

                public:
                    static bool IsBadValue(double val);

                public:
                    static void ComputeCalculatedResults(IN std::vector<FinalResult> &measuredResults,
                                                         OUT std::vector<FinalResult> &calculatedResults,
                                                         IN double passedctHb,
                                                         IN double FiO2,
                                                         IN double patientTemperature,
                                                         IN double ambientPressure,
                                                         IN TestMode testMode,
                                                         IN double patientAge,
                                                         IN Gender gender,
                                                         IN eGFRFormula egfrFormula,
                                                         IN double patientHeight,
                                                         IN AgeCategory ageCategory,
                                                         IN double RQ,
                                                         IN bool calculateAlveolar,
                                                         IN bool theApplymTCO2);

                public:
                    static void ComputeCorrectedResults(IN std::vector<FinalResult> &measuredResults,
                                                        OUT std::vector<FinalResult> &correctedResults,
                                                        IN double patientTemperature,
                                                        IN double ambientPressure,
                                                        IN double FiO2,
                                                        IN double RQ,
                                                        IN bool calculateAlveolar);

                private:
                    static void ComputeCorrectedpH(std::vector<FinalResult> &measuredResults,
                                                   std::vector<FinalResult> &correctedResults,
                                                   double patientTemperature);

                private:
                    static void ComputeCorrectedCarbonDioxide(std::vector<FinalResult> &measuredResults,
                                                              std::vector<FinalResult> &correctedResults,
                                                              double patientTemperature);

                private:
                    static void ComputeCorrectedOxygen(std::vector<FinalResult> &measuredResults,
                                                       std::vector<FinalResult> &correctedResults,
                                                       double patientTemperature);

                private:
                    static void ComputeCalculatedeGFRmdr(std::vector<FinalResult> &measuredResults,
                                                         std::vector<FinalResult> &calculatedResults,
                                                         eGFRType egfrType,
                                                         TestMode testMode,
                                                         double patientAge,
                                                         Gender gender);

                private:
                    static void ComputeCalculatedeGFRjpn(std::vector<FinalResult> &measuredResults,
                                                         std::vector<FinalResult> &calculatedResults,
                                                         eGFRType egfrType,
                                                         TestMode testMode,
                                                         double patientAge,
                                                         Gender gender,
                                                         double patientHeight,
                                                         AgeCategory ageCategory);

                private:
                    static void ComputeCalculatedeGFRckd(std::vector<FinalResult> &measuredResults,
                                                         std::vector<FinalResult> &calculatedResults,
                                                         eGFRType egfrType,
                                                         TestMode testMode,
                                                         double patientAge,
                                                         Gender gender);

                private:
                    static void ComputeCalculatedeGFRswz(std::vector<FinalResult> &measuredResults,
                                                         std::vector<FinalResult> &calculatedResults,
                                                         eGFRType egfrType,
                                                         TestMode testMode,
                                                         double patientAge,
                                                         double patientHeight);

                private:
                    static double ComputeCalculatedActualBicarbonateGivenValues(double pco2Value, double phValue);

                private:
                    static double ComputeAnionGapGivenValues(double sodiumValue,
                                                             double chlorideValue,
                                                             double bicarbValue,
                                                             double mTCO2Value,
                                                             bool applymTCO2);

                private:
                    static void ComputeCalculatedAnionGap(std::vector<FinalResult> &measuredResults,
                                                          std::vector<FinalResult> &calculatedResults,
                                                          AGAPType agapType,
                                                          bool applymTCO2);

                private:
                    static void CopySensorInfo(std::shared_ptr<SensorInfo> &toSensorInfo, const std::shared_ptr<SensorInfo> fromSensorInfo);

                private:
#ifndef CPP_IGNORE_THROW_ON_VALUE_OUT_OF_CSHARP_DECIMAL_RANGE
                    static const double CSharpDecimalMaxValue;   //  Represents the largest possible value for System.Decimal.
                    static const double CSharpDecimalMinValue;   //  Represents the smallest possible value for System.Decimal.
#endif

                    ///////////////////////////////////////////////////////////////////////////////////
                    // Method:      AnalyticalManager::CSharpDecimalCalculation
                    // Parameters:  const double& value
                    //
                    // Return:      double
                    //
                    // Usage:       Evaluates the double value as a C# 'Decimal' value and checks if the
                    //              C++ floating point value would have overflowed in C# if it were a
                    //              System.Decimal value.  If the value is a valid C# Decimal value,
                    //              the passed in value is returned.
                    //
                    //              e.g. double x = AnalyticalManager::CSharpDecimalCalculation(4.55);
                    //                   RESULT> x = 4.55 (a valid value)
                    //
                    // NOTE:        This method is used to mimic the flow of the original C# logic for
                    //              System.Decimal values; C# throws when Decimal values overflow,
                    //              however C++ does not have a type that mirrors this C# type's behavior.
                    static double CSharpDecimalCalculation(const double& value)
                    {
#ifndef CPP_IGNORE_THROW_ON_VALUE_OUT_OF_CSHARP_DECIMAL_RANGE
                        if (isinf(value))
                            throw std::overflow_error("Simulating C# 'Decimal' overflow: INFINITY is undefined for Decimal.");

                        if (isnan(value))
                            throw std::overflow_error("Simulating C# 'Decimal' overflow: NAN is undefined for Decimal.");

                        if ((value > CSharpDecimalMaxValue) || (value < CSharpDecimalMinValue))
                            throw std::overflow_error("Simulating C# 'Decimal' overflow: value out of Decimal range.");
#else
                        // DO NOTHING... we are not trying to mimic C#'s throwing when a Decimal value overflows
#endif
                        return value;
                    }

                    static void CSharpOutOfRange(size_t index, size_t containerSize)
                    {
                        if (index < 0)
                            throw std::out_of_range("Simulating C# out of range: index is out of range (less than zero)");

                        if (index >= containerSize)
                            throw std::out_of_range("Simulating C# out of range: index is out of range (greater than container size)");
                    }
                };
            }
        }
    }
}

#endif //EPOC_AM_NATIVE_BGE_ANALYTICALMANAGER_H
