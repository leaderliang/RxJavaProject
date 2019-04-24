## Switch

```
Switch mSwitch = findViewById(R.id.ios_switch);
        //点击监听
        mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    //开
                    isDefaultAddress = true;
                } else {
                    //关
                    isDefaultAddress = false;
                }

            }
        });
        //设置选中
        mSwitch.setChecked(true);
```