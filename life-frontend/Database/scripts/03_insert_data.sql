-- =============================================
-- CWS.life — Insert Default Data
-- File: 03_insert_data.sql
-- Run this after 02_create_tables.sql
-- =============================================

USE cwslife_db;

-- Insert Super Admin
-- Change email and password before production
INSERT INTO users (full_name, email, password, role)
VALUES ('Dipali Kshirsagar', 'dipali@creativewebsolution.in', 'Admin@123', 'SUPER_ADMIN');

-- ── Events Data ──
INSERT INTO events (title, category, event_date, description, image_url, status)
VALUES

-- Upcoming Events
('Independence Day', 'Event', '2026-08-15',
 'Celebrate the spirit of unity and patriotism at our workplace. Join us for a flag hoisting ceremony, team activities, and a short cultural program honoring our nation journey and values.',
 NULL, 'UPCOMING'),

('Ganesh Chaturthi', 'Festival', '2026-09-14',
 'Join us in welcoming Lord Ganesha with devotion and positivity. Experience traditional rituals, festive decor, and a joyful gathering that brings togetherness to our workplace.',
 NULL, 'UPCOMING'),

('Dussehra', 'Festival', '2026-10-12',
 'Celebrate the triumph of good over evil with a meaningful office gathering. Enjoy cultural activities and symbolic traditions that inspire positivity, growth, and success.',
 NULL, 'UPCOMING'),

('Diwali', 'Festival', '2026-11-22',
 'Celebrate the festival of lights with colorful decorations, cultural performances, sweets, and joyful moments that spread happiness and positivity across the workplace.',
 NULL, 'UPCOMING'),

('Christmas', 'Festival', '2026-12-25',
 'Enjoy the Christmas celebration with festive decorations, fun games, gift exchanges, music, and cheerful moments with colleagues and friends.',
 NULL, 'UPCOMING'),

-- Past Events (with image URLs)
('Gudi Padwa', 'Festival', '2026-03-19',
 'Mark the beginning of a new year with optimism and fresh energy. Join us for a simple celebration embracing tradition, new beginnings, and team bonding.',
 '/uploads/events/gudi.jpeg', 'PAST'),

('Holi', 'Festival', '2026-03-07',
 'Add colors to your workday with a fun-filled Holi celebration. Enjoy music, light activities, and festive treats while strengthening team connections in a joyful environment.',
 '/uploads/events/holi.png', 'PAST'),

('Women Day', 'Event', '2026-03-12',
 'Celebrate and appreciate the incredible women in our workplace. Participate in engaging sessions, recognition activities, and discussions promoting equality and empowerment.',
 '/uploads/events/womesday.png', 'PAST'),

('Diwali Celebration', 'Festival', '2025-10-22',
 'Celebrated the festival of lights with decorations, sweets, cultural performances, and joyful moments shared among team members.',
 '/uploads/events/diwali.jpg', 'PAST'),

('Dussehra Celebration', 'Festival', '2025-10-02',
 'Team members gathered to celebrate the victory of good over evil with cultural activities and festive traditions.',
 '/uploads/events/dusra10.jpg', 'PAST'),

('Republic Day Celebration', 'Event', '2026-01-26',
 'Celebrated Republic Day with patriotic activities, flag hoisting, and team participation honoring the spirit of the nation.',
 '/uploads/events/republic4.jpeg', 'PAST');




-- Jayashree code for job insert
INSERT INTO jobs (
    title,
    department,
    location,
    type,
    experience,
    openings,
    status,
    description,
    roles,
    responsibilities,
    requirements,
    education
) VALUES

(
    'UI/UX Designer',
    'Design',
    'Remote',
    'Full Time',
    '0-2 Years',
    2,
    'ACTIVE',
    'Design intuitive and engaging user experiences for web and mobile products while collaborating with cross-functional teams.',
    'Figma,Wireframing,Prototyping',
    'Create wireframes and prototypes,Collaborate with developers and product managers,Conduct user research and usability testing,Design responsive user interfaces,Maintain design consistency across products',
    'Knowledge of Figma and design tools,Understanding of UI/UX principles,Creative problem-solving skills,Basic prototyping knowledge,Good communication skills',
    'Bachelor''s degree in Design, Computer Applications or related field'
),

(
    'Frontend Developer',
    'Development',
    'Remote',
    'Full Time',
    '0-2 Years',
    3,
    'ACTIVE',
    'Build responsive and high-performance web applications using modern frontend technologies.',
    'HTML,CSS,JavaScript',
    'Develop responsive web pages,Integrate APIs with frontend,Optimize application performance,Fix UI bugs and issues,Collaborate with backend developers',
    'Strong knowledge of HTML CSS and JavaScript,Basic understanding of React or Angular,Responsive design knowledge,Problem-solving skills,Team collaboration skills',
    'Bachelor''s degree in Computer Science or related field'
),

(
    'Backend Developer',
    'Development',
    'Remote',
    'Full Time',
    '0-3 Years',
    2,
    'ACTIVE',
    'Develop scalable backend systems, APIs, and integrations to support robust applications.',
    'Node.js,APIs,Database',
    'Develop REST APIs,Manage database operations,Implement backend business logic,Optimize server performance,Collaborate with frontend team',
    'Knowledge of Java or Node.js,Understanding of REST APIs and databases,Basic SQL knowledge,Debugging and problem-solving skills,Version control using Git',
    'Bachelor''s degree in Computer Science or related field'
),

(
    'Digital Marketing Executive',
    'Marketing',
    'Remote',
    'Full Time',
    '0-3 Years',
    3,
    'ACTIVE',
    'Plan and execute digital marketing campaigns to increase brand visibility and generate quality leads across multiple digital platforms.',
    'SEO,Social Media,Campaigns',
    'Manage social media marketing campaigns,Optimize website content for SEO,Monitor campaign performance and analytics,Create engaging digital content,Coordinate with design and content teams',
    'Knowledge of SEO and social media platforms,Basic understanding of digital marketing tools,Creative thinking and communication skills,Ability to analyze campaign performance,Team collaboration skills',
    'Bachelor''s degree in Marketing, Business Administration or related field'
),

(
    'Data Analyst',
    'Analytics',
    'Remote',
    'Full Time',
    '0-3 Years',
    2,
    'ACTIVE',
    'Analyze business data and generate insights to support strategic decision-making and reporting.',
    'HTML,CSS,Database',
    'Collect and analyze business data,Prepare reports and dashboards,Identify trends and patterns,Work with teams for data-driven decisions,Maintain data accuracy and consistency',
    'Basic knowledge of SQL and Excel,Analytical and problem-solving skills,Understanding of reporting tools,Attention to detail,Communication and teamwork skills',
    'Bachelor''s degree in Computer Science, Statistics or related field'
);


