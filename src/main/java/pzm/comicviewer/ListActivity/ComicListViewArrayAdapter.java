package pzm.comicviewer.ListActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pzm.comicviewer.Comics.CyAndHComic;
import pzm.comicviewer.Comics.DinosaurComic;
import pzm.comicviewer.Comics.IComic;
import pzm.comicviewer.Comics.SMBCComic;
import pzm.comicviewer.Comics.xkcdComic;
import pzm.comicviewer.R;
import pzm.comicviewer.Utils.AnimatedExpandableListView;
import pzm.comicviewer.Utils.App;
import pzm.comicviewer.Utils.ComicEnum;

public class ComicListViewArrayAdapter extends AnimatedExpandableListView.AnimatedExpandableListAdapter {
    private LayoutInflater inflater;

    private List<GroupItem> items;

    public ComicListViewArrayAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setData(List<GroupItem> items) {
        this.items = items;
    }

    @Override
    public IComic getChild(int groupPosition, int childPosition) {
        return items.get(groupPosition).items.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getRealChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View row = convertView;
        ComicItemHolder holder = null;

        row = inflater.inflate(R.layout.comic_list_single_item, parent, false);

        holder = new ComicItemHolder();
        holder.comicItem = getChild(groupPosition, childPosition);
        holder.removePaymentButton = (ImageButton) row.findViewById(R.id.delete_btn);
        holder.favorite_button = (CheckBox) row.findViewById(R.id.favCheckBox);
        holder.enterButton = (ImageButton) row.findViewById(R.id.enter_btn);
        holder.name = (TextView) row.findViewById(R.id.comicTitleText);
        holder.save_button = (CheckBox) row.findViewById(R.id.savCheckBox);

        holder.removePaymentButton.setTag(holder.comicItem);
        holder.favorite_button.setTag(holder.comicItem);
        holder.enterButton.setTag(holder.comicItem);
        holder.name.setTag(holder.comicItem);
        holder.save_button.setTag(holder.comicItem);

        holder.name = (TextView) row.findViewById(R.id.comicTitleText);

        row.setTag(holder);

        setupItem(holder);
        return row;
    }

    @Override
    public int getRealChildrenCount(int groupPosition) {
        return items.get(groupPosition).items.size();
    }

    @Override
    public GroupItem getGroup(int groupPosition) {
        return items.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return items.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder holder;
        GroupItem item = getGroup(groupPosition);
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = inflater.inflate(R.layout.comic_list_section_item, parent, false);
            //holder.title = (TextView) convertView.findViewById(R.id.textTitle);
            holder.logo = (ImageView) convertView.findViewById(R.id.logoimage);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }

        //holder.title.setText(item.title);
        Context ctx = App.getContext();
        int id = ctx.getResources().getIdentifier(ComicEnum.getComicEnum(item.title).getLogoPath(), "drawable", ctx.getPackageName());
        holder.logo.setImageResource(id);

        return convertView;
    }


    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }


    private void setupItem(ComicItemHolder holder) {
        String displayTitle = "";
        if (holder.comicItem instanceof SMBCComic) {
            String url = holder.comicItem.getUrl();
            url = url.substring(url.lastIndexOf("/") + 1, url.length()).replaceAll("-", " ");
            url = url.endsWith(".png") ? url.substring(0, url.length() - 4) : url;
            displayTitle = url.substring(0, 1).toUpperCase() + url.substring(1);
        } else if (holder.comicItem instanceof xkcdComic) {
            String url = holder.comicItem.getUrl();
            url = "#" + url.replaceAll("\\D+", "");
            displayTitle = url;
        } else if (holder.comicItem instanceof DinosaurComic) {
            String url = holder.comicItem.getUrl();
            url = "#" + url.replaceAll("\\D+", "");
            displayTitle = url;
        } else if (holder.comicItem instanceof CyAndHComic) {
            String url = holder.comicItem.getUrl();
            url = "#" + url.replaceAll("\\D+", "");
            displayTitle = url;
        } else {
            displayTitle = holder.comicItem.getTitle();
        }
        holder.name.setText(displayTitle);
        holder.favorite_button.setChecked(holder.comicItem.isFav());
        holder.save_button.setChecked(holder.comicItem.isSaved());
    }

    private static class ComicItemHolder {
        public IComic getComicItem() {
            return comicItem;
        }

        public TextView getName() {
            return name;
        }

        public ImageButton getRemovePaymentButton() {
            return removePaymentButton;
        }

        ImageButton enterButton;
        IComic comicItem;
        TextView name;
        CheckBox favorite_button;
        CheckBox save_button;
        ImageButton removePaymentButton;
    }

    private static class GroupHolder {
        TextView title;
        ImageView logo;
    }

}