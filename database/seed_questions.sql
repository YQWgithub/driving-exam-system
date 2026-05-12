USE driving_exam;

INSERT INTO question (
  category_id, type, content, image_url, options, answer, analysis, difficulty, chapter, knowledge_point
) VALUES
(3, 'single', 'A driver must carry which document while driving?', NULL, '{"A":"Passport","B":"Driver license","C":"Bank card","D":"Student card"}', 'B', 'License is required while driving.', 1, 'chapter1', 'law'),
(3, 'judge', 'Driving without a valid license is illegal.', NULL, '{"A":"True","B":"False"}', 'A', 'A valid license is required by law.', 1, 'chapter1', 'law'),
(3, 'single', 'Which behavior can lead to license suspension?', NULL, '{"A":"Safe driving","B":"Following signs","C":"Serious violation","D":"Using seat belt"}', 'C', 'Serious violations may suspend a license.', 2, 'chapter1', 'law'),
(3, 'judge', 'Seat belt use is optional for short trips.', NULL, '{"A":"True","B":"False"}', 'B', 'Seat belt is required regardless of trip length.', 1, 'chapter1', 'law'),

(4, 'single', 'At a red traffic light, drivers should:', NULL, '{"A":"Speed up","B":"Stop behind line","C":"Turn anytime","D":"Honk and pass"}', 'B', 'Stop behind the stop line at red light.', 1, 'chapter2', 'signal'),
(4, 'judge', 'A yellow light means speed up and pass quickly.', NULL, '{"A":"True","B":"False"}', 'B', 'Yellow requires caution, not acceleration.', 2, 'chapter2', 'signal'),
(4, 'single', 'What does a speed limit sign indicate?', NULL, '{"A":"Minimum speed only","B":"Suggested speed","C":"Maximum allowed speed","D":"No speed limit"}', 'C', 'It indicates the maximum legal speed.', 2, 'chapter2', 'signal'),
(4, 'judge', 'You may ignore lane arrows if traffic is light.', NULL, '{"A":"True","B":"False"}', 'B', 'Lane arrows must be followed.', 2, 'chapter2', 'signal'),

(5, 'single', 'Night meeting traffic should use which light?', NULL, '{"A":"High beam","B":"No light","C":"Low beam","D":"Hazard light"}', 'C', 'Use low beam to avoid glare.', 2, 'chapter3', 'safety'),
(5, 'judge', 'Using mobile phone while driving affects safety.', NULL, '{"A":"True","B":"False"}', 'A', 'Phone use distracts drivers.', 2, 'chapter3', 'safety'),
(5, 'single', 'In school zones, the correct action is:', NULL, '{"A":"Accelerate","B":"Slow down","C":"Random lane change","D":"Continuous horn"}', 'B', 'Slow down and watch for pedestrians.', 2, 'chapter3', 'safety'),
(5, 'judge', 'Following distance should increase in rain.', NULL, '{"A":"True","B":"False"}', 'A', 'Wet roads require longer stopping distance.', 2, 'chapter3', 'safety'),

(6, 'single', 'Before moving off, a driver should first:', NULL, '{"A":"Floor throttle","B":"Check surroundings","C":"Close eyes briefly","D":"Turn music louder"}', 'B', 'Always check surroundings before start.', 1, 'chapter4', 'operation'),
(6, 'judge', 'Mirror check is unnecessary at low speed.', NULL, '{"A":"True","B":"False"}', 'B', 'Mirror check is always necessary.', 2, 'chapter4', 'operation'),
(6, 'single', 'What is the first response to brake failure?', NULL, '{"A":"Panic steering","B":"Keep calm and downshift","C":"Turn off engine immediately","D":"Release steering wheel"}', 'B', 'Downshift and use engine braking.', 4, 'chapter4', 'operation'),
(6, 'judge', 'Hands should stay on steering wheel while driving.', NULL, '{"A":"True","B":"False"}', 'A', 'Maintain steering control at all times.', 1, 'chapter4', 'operation'),

(3, 'single', 'DUI may cause which legal result?', NULL, '{"A":"Warning only","B":"No penalty","C":"Criminal liability","D":"Bonus points"}', 'C', 'DUI can lead to criminal liability.', 4, 'chapter1', 'law'),
(3, 'judge', 'Traffic rules apply only on highways.', NULL, '{"A":"True","B":"False"}', 'B', 'Traffic rules apply on all public roads.', 1, 'chapter1', 'law'),
(3, 'single', 'After a minor collision, driver should:', NULL, '{"A":"Leave immediately","B":"Protect scene and report","C":"Hide vehicle","D":"Argue and block traffic"}', 'B', 'Protect scene and handle legally.', 3, 'chapter1', 'law'),
(3, 'judge', 'Counterfeit plates are legal if temporary.', NULL, '{"A":"True","B":"False"}', 'B', 'Counterfeit plates are illegal.', 3, 'chapter1', 'law'),

(4, 'single', 'What does a stop line mean?', NULL, '{"A":"Optional stop","B":"Mandatory stop position","C":"Parking line","D":"Bus lane"}', 'B', 'Stop before crossing the line when required.', 1, 'chapter2', 'signal'),
(4, 'judge', 'Turn signal should be used before turning.', NULL, '{"A":"True","B":"False"}', 'A', 'Signal in advance before turning.', 1, 'chapter2', 'signal'),
(4, 'single', 'When seeing pedestrian crossing sign, driver should:', NULL, '{"A":"Ignore","B":"Yield to pedestrians","C":"Honk and pass","D":"Speed up"}', 'B', 'Yield at crosswalks.', 2, 'chapter2', 'signal'),
(4, 'judge', 'Reverse driving on one-way road is permitted.', NULL, '{"A":"True","B":"False"}', 'B', 'Reverse driving on one-way roads is prohibited.', 3, 'chapter2', 'signal'),

(5, 'single', 'On icy roads, braking should be:', NULL, '{"A":"Sudden and hard","B":"Gentle and early","C":"Not needed","D":"Only handbrake"}', 'B', 'Brake gently to avoid skidding.', 4, 'chapter3', 'safety'),
(5, 'judge', 'Fatigued driving increases crash risk.', NULL, '{"A":"True","B":"False"}', 'A', 'Fatigue significantly increases risk.', 2, 'chapter3', 'safety'),
(5, 'single', 'In fog, the driver should:', NULL, '{"A":"Use high beam only","B":"Use fog/low beam and reduce speed","C":"Turn off lights","D":"Follow very closely"}', 'B', 'Use proper lights and reduce speed.', 3, 'chapter3', 'safety'),
(5, 'judge', 'Tailgating is a safe way to keep pace.', NULL, '{"A":"True","B":"False"}', 'B', 'Tailgating is unsafe and illegal in many cases.', 2, 'chapter3', 'safety'),

(6, 'single', 'When parking on slope, driver should:', NULL, '{"A":"No brake needed","B":"Apply parking brake and secure vehicle","C":"Leave in neutral always","D":"Turn off wheel lock"}', 'B', 'Secure the vehicle before leaving.', 2, 'chapter4', 'operation'),
(6, 'judge', 'Engine-off coasting is recommended to save fuel.', NULL, '{"A":"True","B":"False"}', 'B', 'Engine-off coasting is dangerous.', 3, 'chapter4', 'operation'),
(6, 'single', 'Correct steering posture helps:', NULL, '{"A":"Reduce control","B":"Increase reaction and control","C":"Increase fatigue only","D":"No effect"}', 'B', 'Proper posture improves control.', 1, 'chapter4', 'operation'),
(6, 'judge', 'Checking tire pressure is part of safe operation.', NULL, '{"A":"True","B":"False"}', 'A', 'Tire pressure checks improve safety.', 1, 'chapter4', 'operation');
