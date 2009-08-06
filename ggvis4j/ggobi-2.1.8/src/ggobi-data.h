/* Generated by GOB (v2.0.15)   (do not edit directly) */

#include <glib.h>
#include <glib-object.h>
#ifndef __GGOBI_DATA_H__
#define __GGOBI_DATA_H__

#ifdef __cplusplus
extern "C" {
#endif /* __cplusplus */




#include "defines.h"
#include "brushing.h"
#include "vartable.h"
#include "fileio.h"

#include <gtk/gtk.h>

typedef struct _Subset {
  gint random_n;
  gint string_pos;
 /*-- adjustments from which to get values for blocksize, everyn --*/
  GtkAdjustment *bstart_adj, *bsize_adj;
  GtkAdjustment *estart_adj, *estep_adj;
} Subset;


typedef struct _Jitterd {
  gfloat factor;
  gboolean type;
  gboolean convex;
  gfloat *jitfacv;
} Jitterd;

typedef struct _Varpanel_cboxd {
  GtkWidget *ebox;   /*-- child1 of pane widget --*/
  GtkWidget *swin;   /*-- child of ebox --*/
  GtkWidget *vbox;   /*-- child of swin --*/
  GSList *box;       /*-- single column of hboxes --*/
} Varpanel_cboxd;

typedef struct _Varpanel_circd {
  GtkWidget *ebox;        /*-- child2 of pane widget --*/
  GtkWidget *vbox;        /*-- child of ebox --*/
  GtkWidget *swin, *hbox; /*-- children of vbox --*/
  GtkWidget *table;       /*-- sole child of swin; now a vbox --*/
  GtkWidget *manip_btn, *freeze_btn; /*-- children of hbox --*/

  GdkCursor *cursor;
  gint jcursor;

 /*-- components and properties of the table --*/
  GSList *vb, *da, *label;
  GSList *da_pix;         /*-- backing pixmaps --*/
  gint nvars;
} Varpanel_circd;

typedef struct _Varpaneld {
  GtkWidget *hpane;  /*-- child of the ebox --*/
} Varpaneld;

/*-------------------- transformation --------------------------------*/

/* sphering transformation */
typedef struct _Sphere_d {
  vector_i vars;        /*-- vars available to be sphered --*/
  vector_i vars_sphered;/*-- vars that have been sphered --*/
  gint npcs;      /*-- the first npcs vars of vars will be sphered --*/
  vector_i pcvars;/*-- vars into which sphered data is written --*/

  vector_f eigenval;
  array_d eigenvec;
  array_f vc;
  vector_f tform_mean;
  vector_f tform_stddev;

  gboolean vars_stdized;
} Sphere_d;


typedef struct _EdgeData {
  gint n;
  SymbolicEndpoints *sym_endpoints;
  GList *endpointList;   
    /* a list of endpointsd elements corresponding to the resolved 
       record ids relative to a given datad. This is akin to a table
       indexed by datad elements. */

  gint nxed_by_brush;
  vector_b xed_by_brush;
} EdgeData;

typedef struct _BrushBins {
  gint nbins;
  bin_struct **binarray;
  icoords bin0, bin1;
} BrushBins;




/*
 * Type checking and casting macros
 */
#define GGOBI_TYPE_DATA	(ggobi_data_get_type())
#define GGOBI_DATA(obj)	G_TYPE_CHECK_INSTANCE_CAST((obj), ggobi_data_get_type(), GGobiData)
#define GGOBI_DATA_CONST(obj)	G_TYPE_CHECK_INSTANCE_CAST((obj), ggobi_data_get_type(), GGobiData const)
#define GGOBI_DATA_CLASS(klass)	G_TYPE_CHECK_CLASS_CAST((klass), ggobi_data_get_type(), GGobiDataClass)
#define GGOBI_IS_DATA(obj)	G_TYPE_CHECK_INSTANCE_TYPE((obj), ggobi_data_get_type ())

#define GGOBI_DATA_GET_CLASS(obj)	G_TYPE_INSTANCE_GET_CLASS((obj), ggobi_data_get_type(), GGobiDataClass)

/*
 * Main object structure
 */
#ifndef __TYPEDEF_GGOBI_DATA__
#define __TYPEDEF_GGOBI_DATA__
typedef struct _GGobiData GGobiData;
#endif
struct _GGobiData {
	GObject __parent__;
	/*< public >*/
	const gchar * name;
	const gchar * nickname;
	InputDescription * input;
	guint nrows;
	gint ncols;
	gboolean missings;
	struct _ggobid * gg;
	GArray * rowlab;
	GHashTable * idTable;
	char ** rowIds;
	vartabled * linkvar_vt;
	Subset subset;
	Jitterd jitter;
	Varpanel_cboxd vcbox_ui;
	Varpanel_circd vcirc_ui;
	Varpaneld varpanel_ui;
	Sphere_d sphere;
	EdgeData edge;
	BrushBins brush;
	GSList * vartable;
	GtkWidget * vartable_tree_view[all_vartypes];
	GtkTreeModel * vartable_tree_model;
	array_f raw;
	array_f tform;
	array_g world;
	array_g jitdata;
	array_s missing;
	gboolean missings_show_p;
	vector_i rows_in_plot;
	gint nrows_in_plot;
	vector_b sampled;
	vector_b excluded;
	symbol_cell symbol_table[NGLYPHTYPES][NGLYPHSIZES][MAXNCOLORS];
	GtkWidget * cluster_table;
	gint nclusters;
	clusterd * clusv;
	clusteruid * clusvui;
	vector_i clusterid;
	gint npts_under_brush;
	vector_b pts_under_brush;
	vector_s color;
	vector_s color_now;
	vector_s color_prev;
	vector_b hidden;
	vector_b hidden_now;
	vector_b hidden_prev;
	vector_g glyph;
	vector_g glyph_now;
	vector_g glyph_prev;
	gint nearest_point;
	gint nearest_point_prev;
	GSList * sticky_ids;
	GSList * movepts_history;
};

/*
 * Class definition
 */
typedef struct _GGobiDataClass GGobiDataClass;
struct _GGobiDataClass {
	GObjectClass __parent__;
	/*signal*/void (* rows_in_plot_changed) (GGobiData * self, int arg1, int arg2, ggobid * arg3);
	/*signal*/void (* col_name_changed) (GGobiData * self, int j);
};


/*
 * Public methods
 */
GType	ggobi_data_get_type	(void);
guint 	ggobi_data_get_nrows	(GGobiData * self);
void 	ggobi_data_set_nrows	(GGobiData * self,
					guint val);
guint 	ggobi_data_get_ncols	(GGobiData * self);
void 	ggobi_data_set_ncols	(GGobiData * self,
					guint val);
void 	ggobi_data_col_name_changed	(GGobiData * self,
					int j);
GGobiData * 	ggobi_data_new	(guint nrows,
					guint ncols);
gchar * 	ggobi_data_get_name	(GGobiData * self);
void 	ggobi_data_set_name	(GGobiData * self,
					gchar * name,
					gchar * nickname);
guint 	ggobi_data_get_n_cols	(GGobiData * self);
guint 	ggobi_data_get_n_rows	(GGobiData * self);
gchar * 	ggobi_data_get_col_name	(GGobiData * self,
					guint j);
void 	ggobi_data_set_col_name	(GGobiData * self,
					guint j,
					gchar * value);
gchar * 	ggobi_data_get_transformed_col_name	(GGobiData * self,
					guint j);
void 	ggobi_data_set_transformed_col_name	(GGobiData * self,
					guint j,
					gchar * value);
void 	ggobi_data_set_missing	(GGobiData * self,
					guint i,
					guint j);
gboolean 	ggobi_data_is_missing	(GGobiData * self,
					guint i,
					guint j);
gboolean 	ggobi_data_has_missings	(GGobiData * self);
guint 	ggobi_data_get_col_n_missing	(GGobiData * self,
					guint j);
void 	ggobi_data_set_raw_values	(GGobiData * self,
					guint j,
					gdouble * values);
void 	ggobi_data_set_raw_value	(GGobiData * self,
					guint i,
					guint j,
					gdouble value);
gchar * 	ggobi_data_get_string_value	(GGobiData * self,
					guint i,
					guint j,
					gboolean transformed);
gboolean 	ggobi_data_has_edges	(GGobiData * self);
gboolean 	ggobi_data_has_variables	(GGobiData * self);

/*
 * Signal connection wrapper macros
 */
#if defined(__GNUC__) && !defined(__STRICT_ANSI__)
#define ggobi_data_connect__rows_in_plot_changed(object,func,data)	g_signal_connect(GGOBI_DATA(__extension__ ({GGobiData *___object = (object); ___object; })),"rows_in_plot_changed",(GCallback) __extension__ ({void (* ___rows_in_plot_changed) (GGobiData * ___fake___self, int ___fake___arg1, int ___fake___arg2, ggobid * ___fake___arg3, gpointer ___data ) = (func); ___rows_in_plot_changed; }), (data))
#define ggobi_data_connect_after__rows_in_plot_changed(object,func,data)	g_signal_connect_after(GGOBI_DATA(__extension__ ({GGobiData *___object = (object); ___object; })),"rows_in_plot_changed",(GCallback) __extension__ ({void (* ___rows_in_plot_changed) (GGobiData * ___fake___self, int ___fake___arg1, int ___fake___arg2, ggobid * ___fake___arg3, gpointer ___data ) = (func); ___rows_in_plot_changed; }), (data))
#define ggobi_data_connect_data__rows_in_plot_changed(object,func,data,destroy_data,flags)	g_signal_connect_data(GGOBI_DATA(__extension__ ({GGobiData *___object = (object); ___object; })),"rows_in_plot_changed",(GCallback) __extension__ ({void (* ___rows_in_plot_changed) (GGobiData * ___fake___self, int ___fake___arg1, int ___fake___arg2, ggobid * ___fake___arg3, gpointer ___data ) = (func); ___rows_in_plot_changed; }), (data), (destroy_data), (GConnectFlags)(flags))
#define ggobi_data_connect__col_name_changed(object,func,data)	g_signal_connect(GGOBI_DATA(__extension__ ({GGobiData *___object = (object); ___object; })),"col_name_changed",(GCallback) __extension__ ({void (* ___col_name_changed) (GGobiData * ___fake___self, int ___fake___j, gpointer ___data ) = (func); ___col_name_changed; }), (data))
#define ggobi_data_connect_after__col_name_changed(object,func,data)	g_signal_connect_after(GGOBI_DATA(__extension__ ({GGobiData *___object = (object); ___object; })),"col_name_changed",(GCallback) __extension__ ({void (* ___col_name_changed) (GGobiData * ___fake___self, int ___fake___j, gpointer ___data ) = (func); ___col_name_changed; }), (data))
#define ggobi_data_connect_data__col_name_changed(object,func,data,destroy_data,flags)	g_signal_connect_data(GGOBI_DATA(__extension__ ({GGobiData *___object = (object); ___object; })),"col_name_changed",(GCallback) __extension__ ({void (* ___col_name_changed) (GGobiData * ___fake___self, int ___fake___j, gpointer ___data ) = (func); ___col_name_changed; }), (data), (destroy_data), (GConnectFlags)(flags))
#else /* __GNUC__ && !__STRICT_ANSI__ */
#define ggobi_data_connect__rows_in_plot_changed(object,func,data)	g_signal_connect(GGOBI_DATA(object),"rows_in_plot_changed",(GCallback)(func),(data))
#define ggobi_data_connect_after__rows_in_plot_changed(object,func,data)	g_signal_connect_after(GGOBI_DATA(object),"rows_in_plot_changed",(GCallback)(func),(data))
#define ggobi_data_connect_data__rows_in_plot_changed(object,func,data,destroy_data,flags)	g_signal_connect_data(GGOBI_DATA(object),"rows_in_plot_changed",(GCallback)(func),(data),(destroy_data),(GConnectFlags)(flags))
#define ggobi_data_connect__col_name_changed(object,func,data)	g_signal_connect(GGOBI_DATA(object),"col_name_changed",(GCallback)(func),(data))
#define ggobi_data_connect_after__col_name_changed(object,func,data)	g_signal_connect_after(GGOBI_DATA(object),"col_name_changed",(GCallback)(func),(data))
#define ggobi_data_connect_data__col_name_changed(object,func,data,destroy_data,flags)	g_signal_connect_data(GGOBI_DATA(object),"col_name_changed",(GCallback)(func),(data),(destroy_data),(GConnectFlags)(flags))
#endif /* __GNUC__ && !__STRICT_ANSI__ */


/*
 * Argument wrapping macros
 */
#if defined(__GNUC__) && !defined(__STRICT_ANSI__)
#define GGOBI_DATA_PROP_NROWS(arg)    	"nrows", __extension__ ({guint z = (arg); z;})
#define GGOBI_DATA_GET_PROP_NROWS(arg)	"nrows", __extension__ ({guint *z = (arg); z;})
#define GGOBI_DATA_PROP_NCOLS(arg)    	"ncols", __extension__ ({guint z = (arg); z;})
#define GGOBI_DATA_GET_PROP_NCOLS(arg)	"ncols", __extension__ ({guint *z = (arg); z;})
#else /* __GNUC__ && !__STRICT_ANSI__ */
#define GGOBI_DATA_PROP_NROWS(arg)    	"nrows",(guint )(arg)
#define GGOBI_DATA_GET_PROP_NROWS(arg)	"nrows",(guint *)(arg)
#define GGOBI_DATA_PROP_NCOLS(arg)    	"ncols",(guint )(arg)
#define GGOBI_DATA_GET_PROP_NCOLS(arg)	"ncols",(guint *)(arg)
#endif /* __GNUC__ && !__STRICT_ANSI__ */




void freeLevelHashEntry(gpointer key, gpointer value, gpointer data);

/*-- used as an attribute of variable notebooks --*/
typedef enum {no_edgesets, edgesets_only, all_datatypes} datatyped;

extern endpointsd *resolveEdgePoints(GGobiData *e, GGobiData *d);
void unresolveAllEdgePoints(GGobiData *e);

void datad_record_ids_set(GGobiData *d, gchar **ids, gboolean duplicate);
void ggobi_data_set_row_labels(GGobiData *d, gchar **labels);


#ifdef __cplusplus
}
#endif /* __cplusplus */

#endif