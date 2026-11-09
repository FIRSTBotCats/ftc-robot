// BotCats #30521 - REV Driver Hub (REV-31-1596) rugged TPU case
// Parametric. All dimensions in mm.
// Orientation: X = hub length, Y = hub width (+Y = USB port edge), Z = thickness (+Z = screen side).
// Print screen-side UP, back flat on the bed, no supports. See README.md for print settings.

/* [Driver Hub body: measure yours and adjust] */
hub_L   = 133.35;   // REV spec 5.25 in
hub_W   = 85.73;    // REV spec 3.375 in
hub_H   = 25.4;     // REV spec 1.0 in
hub_r   = 4;        // cavity corner radius (keep <= the hub's real corner radius)
clr     = 0.4;      // clearance per side (TPU: 0.3-0.5)

/* [Shell] */
wall    = 3.2;      // side wall
back_t  = 2.4;      // back plate
lip_in  = 4.0;      // how far the front lip covers the bezel (screen bezel is ~10 mm)
lip_ch  = 2.0;      // 45 deg chamfer under the lip (support-free)
lip_t   = 1.6;      // lip thickness above chamfer; lip stands proud of the glass
R_out   = 10;       // outer corner radius
e_out   = 3;        // outer edge rounding
bump    = 1.6;      // extra corner bumper protrusion

/* [Port bay (protects plugs)] */
bay_w       = 112;  // width of the opening along the port edge
bay_x       = 0;    // shift the bay along X if your ports are off-center
bay_d       = 32;   // depth from hub edge to the cable comb (> plug stick-out)
comb_t      = 5;    // thickness of the far cable-comb bar

/* [Cable slots: EDIT THESE to match your cables] */
// One entry per slot: [x_from_left, z_from_back, seat_d, mouth_w]
//   x_from_left : mm from the hub's LEFT end (screen facing you, ports on top) to the cable centre
//   z_from_back : mm from the hub's BACK face to the cable centre (hub is 25.4 thick; 12.7 = middle)
//   seat_d      : cable seat diameter, ~0.5 mm over the cable OD (gamepad cables ~3.5-4.5)
//   mouth_w     : slot opening, ~0.5-1 mm UNDER the cable OD so the cable snaps in and stays
// Add, remove or move entries freely. Slots outside the bay width are ignored by the bay
// but still cut the comb, so keep x between (hub_L-bay_w)/2+bay_x and (hub_L+bay_w)/2+bay_x.
slots = [
    [ 24.7, 12.7, 5.0, 3.6],   // gamepad 1
    [ 52.7, 12.7, 5.0, 3.6],   // gamepad 2
    [ 80.7, 12.7, 5.0, 3.6],   // gamepad 3
    [108.7, 12.7, 5.0, 3.6],   // charger (USB-C)
];

/* [Extra cutouts: power button, Ethernet, etc. (empty by default)] */
// One entry per cutout: [face, pos, z_from_back, w, h]
//   face        : "top" (port edge), "bottom", "left", "right"
//   pos         : mm along that edge to the cutout centre; top/bottom measured from the LEFT end,
//                 left/right measured from the BOTTOM edge
//   z_from_back : mm from the hub's back face to the cutout centre
//   w, h        : cutout width along the edge and height through the thickness
// Example: ["bottom", 40, 12.7, 18, 16]  // Ethernet on the bottom edge, 40 mm from the left
cutouts = [];

/* [Preview helpers (turn off before exporting STL)] */
show_hub = false;   // ghost of the Driver Hub in the cavity

/* [Extras] */
cat_ears   = true;
ear_hole_d = 5;     // lanyard / zip-tie hole through each ear
vents      = true;  // hex vents on the back
label      = "BOTCATS";
label2     = "#30521";

$fn = 48;

// ---------- derived ----------
cav_L = hub_L + 2*clr;
cav_W = hub_W + 2*clr;
cav_H = hub_H + clr;
H     = back_t + cav_H + lip_ch + lip_t;
x_half = cav_L/2 + wall;
y_bot  = -(cav_W/2 + wall);
y_top  = cav_W/2 + bay_d + comb_t;
zc     = back_t + cav_H/2;          // port / cable centre height

// ---------- helpers ----------
module rrect(x0, y0, x1, y1, r, h) {          // 2.5D rounded rectangle
    linear_extrude(h) offset(r) offset(-r)
        translate([x0, y0]) square([x1 - x0, y1 - y0]);
}
module rslab(x0, y0, x1, y1, h, r, e) {        // fully rounded slab
    hull() for (x = [x0 + r, x1 - r], y = [y0 + r, y1 - r])
        translate([x, y, 0]) rotate_extrude($fn = 48)
            hull() for (z = [e, h - e]) translate([r - e, z]) circle(e, $fn = 24);
}
module post(x, y, r, h, e) {                   // rounded vertical post
    ee = min(e, r/2);
    translate([x, y, 0]) rotate_extrude($fn = 40)
        hull() for (z = [ee, h - ee]) translate([r - ee, z]) circle(ee, $fn = 20);
}

// ---------- body ----------
module outer() {
    rslab(-x_half, y_bot, x_half, y_top, H, R_out, e_out);
    // corner bumpers
    for (sx = [-1, 1], cy = [y_bot, y_top]) {
        cx = sx * x_half;
        hull() {
            post(cx - sx*(R_out - bump), cy + (cy == y_bot ? 1 : -1)*(R_out - bump), R_out, H, e_out);
            post(cx - sx*(R_out + 12), cy + (cy == y_bot ? 1 : -1)*(R_out - 0.5), R_out - 1, H, e_out);
            post(cx - sx*(R_out - 0.5), cy + (cy == y_bot ? 1 : -1)*(R_out + 12), R_out - 1, H, e_out);
        }
    }
    if (cat_ears) for (s = [-1, 1]) ear(s);
}

module ear(s) {
    hull() {
        post(s*(x_half - 8),  y_top - 6, 6, H, e_out);
        post(s*(x_half - 34), y_top - 4, 6, H, e_out);
        post(s*(x_half - 10), y_top + 16, 3.5, H, e_out);
    }
}

module cavity() {
    // hub pocket
    translate([0, 0, back_t]) rrect(-cav_L/2, -cav_W/2, cav_L/2, cav_W/2, hub_r, cav_H);
    // 45 deg chamfer under lip, then the screen window
    hull() {
        translate([0, 0, back_t + cav_H - 0.01]) rrect(-cav_L/2, -cav_W/2, cav_L/2, cav_W/2, hub_r, 0.01);
        translate([0, 0, back_t + cav_H + lip_ch]) rrect(-cav_L/2 + lip_in, -cav_W/2 + lip_in,
                                                         cav_L/2 - lip_in, cav_W/2 - lip_in, hub_r, 0.01);
    }
    translate([0, 0, back_t + cav_H + lip_ch - 0.01])
        rrect(-cav_L/2 + lip_in, -cav_W/2 + lip_in, cav_L/2 - lip_in, cav_W/2 - lip_in, hub_r, lip_t + 1);
}

module port_bay() {
    // open-front bay: plugs sit below the rim, never the first thing to hit the floor
    translate([0, 0, back_t])
        rrect(bay_x - bay_w/2, cav_W/2 - 6, bay_x + bay_w/2, cav_W/2 + bay_d, 4, H);
    // cable comb: keyhole slots, cable snaps in from the front
    for (sl = slots) let(x = sl[0] - hub_L/2, zs = back_t + clr/2 + sl[1],
                          cable_d = sl[2], cable_mouth = sl[3])
      translate([x, cav_W/2 + bay_d - 1, 0]) {
        translate([0, 0, zs]) rotate([-90, 0, 0]) cylinder(d = cable_d, h = comb_t + 30);
        translate([-cable_mouth/2, 0, zs]) cube([cable_mouth, comb_t + 30, H]);
        // flare the mouth so cables drop in easily
        translate([0, 0, H - 1.2]) hull() {
            translate([-cable_mouth/2, 0, 0]) cube([cable_mouth, comb_t + 30, 0.01]);
            translate([-cable_mouth/2 - 1.2, 0, 1.2]) cube([cable_mouth + 2.4, comb_t + 30, 0.6]);
        }
    }
}

module back_vents() {
    p = 9.5; d = 7.2;
    intersection() {
        translate([0, 0, -1]) rrect(-cav_L/2 + 12, -cav_W/2 + 10, cav_L/2 - 12, cav_W/2 - 10, 6, back_t + 2);
        for (i = [-8:8], j = [-5:5])
            translate([i*p + (j % 2 == 0 ? 0 : p/2), j*p*0.866, -1])
                cylinder(d = d, h = back_t + 2, $fn = 6);
    }
}

module back_label() {
    // debossed into the bed face under the port bay, reads correctly from behind
    yc = cav_W/2 + bay_d/2 + 1;
    translate([bay_x, yc, -0.01]) mirror([1, 0, 0]) linear_extrude(0.8) {
        translate([0, 3.5]) text(label,  size = 9,  font = "Liberation Sans:style=Bold", halign = "center", valign = "center");
        translate([0, -8])  text(label2, size = 6.5, font = "Liberation Sans:style=Bold", halign = "center", valign = "center");
    }
}

module ear_holes() {
    if (cat_ears) for (s = [-1, 1])
        translate([s*(x_half - 14), y_top + 4, -1]) cylinder(d = ear_hole_d, h = H + 2);
}

module extra_cutouts() {
    for (c = cutouts) let(f = c[0], pos = c[1], z = back_t + clr/2 + c[2], w = c[3], h = c[4], d = wall + bump + 20) {
        if (f == "top")    translate([pos - hub_L/2 - w/2,  cav_W/2 - 1, z - h/2]) cube([w, d, h]);
        if (f == "bottom") translate([pos - hub_L/2 - w/2, -cav_W/2 - d + 1, z - h/2]) cube([w, d, h]);
        if (f == "left")   translate([-cav_L/2 - d + 1, pos - hub_W/2 - w/2, z - h/2]) cube([d, w, h]);
        if (f == "right")  translate([ cav_L/2 - 1,     pos - hub_W/2 - w/2, z - h/2]) cube([d, w, h]);
    }
}

if (show_hub) %translate([-hub_L/2, -hub_W/2, back_t + clr/2]) cube([hub_L, hub_W, hub_H]);

difference() {
    outer();
    extra_cutouts();
    cavity();
    port_bay();
    if (vents) back_vents();
    back_label();
    ear_holes();
}
